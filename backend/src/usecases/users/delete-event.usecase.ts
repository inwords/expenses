import {Injectable} from '@nestjs/common';
import {UseCase} from '#packages/use-case';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {IEvent} from '#domain/entities/event.entity';
import {ensureEventAvailable} from './utils/event-availability';

interface Input {
  eventId: IEvent['id'];
  pinCode: IEvent['pinCode'];
}

type Output = void;

@Injectable()
export class DeleteEventUseCase implements UseCase<Input, Output> {
  constructor(private readonly rDataService: RelationalDataServiceAbstract) {
  }

  public async execute({eventId, pinCode}: Input) {
    await this.rDataService.transaction(async (ctx) => {
      await ensureEventAvailable(this.rDataService, eventId, pinCode, {
        ctx,
        lock: 'pessimistic_write',
        onLocked: 'nowait',
      });

      const deletedAt = new Date();

      // Hard delete related data (legal compliance)
      await this.rDataService.expense.deleteByEventId(eventId, {ctx});
      await this.rDataService.userInfo.deleteByEventId(eventId, {ctx});

      // Soft delete event (keep name and pinCode for 410 response)
      await this.rDataService.event.softDeleteById(eventId, deletedAt, {ctx});
    });
  }
}
