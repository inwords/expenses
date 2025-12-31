import {Injectable} from '@nestjs/common';
import {UseCase} from '#packages/use-case';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {EventServiceAbstract} from '#domain/abstracts/event-service/event-service';
import {IEvent} from '#domain/entities/event.entity';

type Input = {
  eventId: IEvent['id'];
  pinCode: string;
};

type Output = {
  id: IEvent['id'];
  deletedAt: Date;
};

@Injectable()
export class DeleteEventUseCase implements UseCase<Input, Output> {
  constructor(
    private readonly rDataService: RelationalDataServiceAbstract,
    private readonly eventService: EventServiceAbstract,
  ) {}

  public async execute({eventId, pinCode}: Input): Promise<Output> {
    return this.rDataService.transaction(async (ctx) => {
      const [event] = await this.rDataService.event.findById(eventId, {
        ctx,
        lock: 'pessimistic_write',
        onLocked: 'nowait',
      });

      this.eventService.validateEvent(event, pinCode);

      const deletedAt = new Date();

      // Выполняем soft delete
      await this.rDataService.event.update(
        eventId,
        {
          deletedAt,
          updatedAt: new Date(),
        },
        {ctx},
      );

      return {
        id: eventId,
        deletedAt,
      };
    });
  }
}
