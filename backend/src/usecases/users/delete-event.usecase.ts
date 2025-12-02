import {HttpException, HttpStatus, Injectable} from '@nestjs/common';
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
  constructor(private readonly rDataService: RelationalDataServiceAbstract) {}

  public async execute({eventId, pinCode}: Input) {
    await this.rDataService.transaction(async (ctx) => {
      const event = await ensureEventAvailable(this.rDataService, eventId, {
        ctx,
        lock: 'pessimistic_write',
      });

      if (event.pinCode !== pinCode) {
        throw new HttpException(
          {
            status: HttpStatus.FORBIDDEN,
            error: 'Invalid event pin code',
          },
          HttpStatus.FORBIDDEN,
        );
      }

      await this.rDataService.expense.deleteByEventId(eventId, {ctx});
      await this.rDataService.user.deleteByEventId(eventId, {ctx});
      await this.rDataService.event.deleteById(eventId, {ctx});
      await this.rDataService.deletedEvent.insert({eventId, deletedAt: new Date()}, {ctx});
    });
  }
}
