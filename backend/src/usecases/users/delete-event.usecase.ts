import {
  HttpException,
  HttpStatus,
  Injectable,
  Logger,
  OnModuleDestroy,
  OnModuleInit,
} from '@nestjs/common';
import {SchedulerRegistry} from '@nestjs/schedule';
import {UseCase} from '#packages/use-case';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {IEvent} from '#domain/entities/event.entity';
import {
  DELETION_GRACE_PERIOD_MS,
  ensureEventAvailable,
  isDeletionGracePeriodElapsed,
} from './utils/event-availability';
import {ITransactionWithLock} from '#domain/abstracts/relational-data-service/types';

interface Input {
  eventId: IEvent['id'];
  pinCode: IEvent['pinCode'];
}

type Output = void;

@Injectable()
export class DeleteEventUseCase
  implements UseCase<Input, Output>, OnModuleDestroy, OnModuleInit
{
  private readonly logger = new Logger(DeleteEventUseCase.name);

  constructor(
    private readonly rDataService: RelationalDataServiceAbstract,
    private readonly schedulerRegistry: SchedulerRegistry,
  ) {}

  public async onModuleInit() {
    try {
      const [softDeletedEvents] = await this.rDataService.event.findSoftDeleted();

      for (const event of softDeletedEvents) {
        if (!event.deletedAt) {
          continue;
        }

        if (isDeletionGracePeriodElapsed(event.deletedAt)) {
          try {
            await this.finalizeDeletion(event.id);
          } catch (error) {
            this.logger.error(
              `Failed to finalize deletion for event ${event.id} during initialization`,
              error,
            );
          }
          continue;
        }

        this.scheduleFinalDeletion(event.id, event.deletedAt);
      }
    } catch (error) {
      this.logger.error('Failed to reschedule pending deletions on startup', error);
    }
  }

  public async execute({eventId, pinCode}: Input) {
    let deletedAt: Date | null = null;
    let finalizedWithinRequest = false;

    await this.rDataService.transaction(async (ctx) => {
      const event = await ensureEventAvailable(
        this.rDataService,
        eventId,
        {
          ctx,
          lock: 'pessimistic_write',
        },
        {allowPendingDeletion: true},
      );

      if (event.pinCode !== pinCode) {
        throw new HttpException(
          {
            status: HttpStatus.FORBIDDEN,
            error: 'Invalid event pin code',
          },
          HttpStatus.FORBIDDEN,
        );
      }

      const eventDeletedAt = event.deletedAt ?? new Date();

      if (!event.deletedAt) {
        await this.rDataService.event.setDeletedAt(eventId, eventDeletedAt, {ctx});
      }

      deletedAt = eventDeletedAt;

      if (isDeletionGracePeriodElapsed(eventDeletedAt)) {
        await this.finalizeDeletion(eventId, ctx);
        finalizedWithinRequest = true;
      }
    });

    if (deletedAt && !finalizedWithinRequest) {
      this.scheduleFinalDeletion(eventId, deletedAt);
    }
  }

  public onModuleDestroy() {
    for (const timeoutName of this.schedulerRegistry.getTimeouts()) {
      const timeout = this.schedulerRegistry.getTimeout(timeoutName);
      clearTimeout(timeout);
      this.schedulerRegistry.deleteTimeout(timeoutName);
    }
  }

  private scheduleFinalDeletion(eventId: IEvent['id'], deletedAt: Date) {
    const elapsed = Date.now() - new Date(deletedAt).getTime();
    const delay = Math.max(DELETION_GRACE_PERIOD_MS - elapsed, 0);

    const timeoutName = this.getTimeoutName(eventId);

    try {
      this.schedulerRegistry.deleteTimeout(timeoutName);
    } catch (error) {
      this.logger.debug(`No existing timeout to clear for ${timeoutName}`);
    }

    const timer = setTimeout(async () => {
      try {
        await this.finalizeDeletion(eventId);
      } catch (error) {
        this.logger.error(`Failed to finalize deletion for event ${eventId}`, error);
      } finally {
        try {
          this.schedulerRegistry.deleteTimeout(timeoutName);
        } catch (cleanupError) {
          this.logger.warn(`Failed to delete timeout ${timeoutName}`, cleanupError as Error);
        }
      }
    }, delay);

    if (timer.unref) {
      timer.unref();
    }

    this.schedulerRegistry.addTimeout(timeoutName, timer);
  }

  private getTimeoutName(eventId: IEvent['id']): string {
    return `delete-event-${eventId}`;
  }

  private async finalizeDeletion(
    eventId: IEvent['id'],
    ctx?: ITransactionWithLock['ctx'],
  ): Promise<void> {
    const run = async (transactionCtx: ITransactionWithLock['ctx']) => {
      const [currentEvent] = await this.rDataService.event.findById(eventId, {
        ctx: transactionCtx,
        lock: 'pessimistic_write',
      });

      if (!currentEvent || !currentEvent.deletedAt) {
        return;
      }

      if (!isDeletionGracePeriodElapsed(currentEvent.deletedAt)) {
        return;
      }

      await this.rDataService.expense.deleteByEventId(eventId, {ctx: transactionCtx});
      await this.rDataService.user.deleteByEventId(eventId, {ctx: transactionCtx});
      await this.rDataService.event.deleteById(eventId, {ctx: transactionCtx});
      await this.rDataService.deletedEvent.insert(
        {eventId, deletedAt: currentEvent.deletedAt},
        {ctx: transactionCtx},
      );
    };

    if (ctx) {
      await run(ctx);
      return;
    }

    await this.rDataService.transaction(async (transactionCtx) => run(transactionCtx));
  }
}
