import {HttpException, HttpStatus, Injectable, Logger, OnModuleDestroy} from '@nestjs/common';
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
export class DeleteEventUseCase implements UseCase<Input, Output>, OnModuleDestroy {
  private readonly logger = new Logger(DeleteEventUseCase.name);
  private readonly finalizationTimers = new Map<string, NodeJS.Timeout>();

  constructor(private readonly rDataService: RelationalDataServiceAbstract) {}

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

      if (!event || event.pinCode !== pinCode) {
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
    this.finalizationTimers.forEach((timer) => clearTimeout(timer));
    this.finalizationTimers.clear();
  }

  private scheduleFinalDeletion(eventId: IEvent['id'], deletedAt: Date) {
    const elapsed = Date.now() - new Date(deletedAt).getTime();
    const delay = Math.max(DELETION_GRACE_PERIOD_MS - elapsed, 0);

    const existingTimer = this.finalizationTimers.get(eventId);
    if (existingTimer) {
      clearTimeout(existingTimer);
    }

    const timer = setTimeout(() => {
      this.finalizationTimers.delete(eventId);
      this.finalizeDeletion(eventId).catch((error) =>
        this.logger.error(`Failed to finalize deletion for event ${eventId}`, error),
      );
    }, delay);

    if (timer.unref) {
      timer.unref();
    }

    this.finalizationTimers.set(eventId, timer);
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
