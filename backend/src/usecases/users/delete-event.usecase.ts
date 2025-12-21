import {HttpException, HttpStatus, Injectable} from '@nestjs/common';
import {UseCase} from '#packages/use-case';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
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
  constructor(private readonly rDataService: RelationalDataServiceAbstract) {}

  public async execute({eventId, pinCode}: Input): Promise<Output> {
    return this.rDataService.transaction(async (ctx) => {
      // Блокируем event с pessimistic_write для предотвращения race condition
      // Если кто-то пытается добавить expense, он заблокируется и будет ждать пока мы удалим event
      const [event] = await this.rDataService.event.findById(eventId, {
        ctx,
        lock: 'pessimistic_write',
        onLocked: 'nowait',
      });

      if (!event) {
        throw new HttpException(
          {
            status: HttpStatus.NOT_FOUND,
            error: `Event with id ${eventId} not found`,
          },
          HttpStatus.NOT_FOUND,
        );
      }

      // Проверяем что event еще не удален
      if (event.deletedAt !== null) {
        throw new HttpException(
          {
            status: HttpStatus.GONE,
            error: `Event with id ${eventId} has already been deleted`,
          },
          HttpStatus.GONE,
        );
      }

      // Проверяем PIN код
      if (event.pinCode !== pinCode) {
        throw new HttpException(
          {
            status: HttpStatus.FORBIDDEN,
            error: `Invalid PIN code for event ${eventId}`,
          },
          HttpStatus.FORBIDDEN,
        );
      }

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
