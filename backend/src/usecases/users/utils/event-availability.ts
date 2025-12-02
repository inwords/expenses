import {HttpException, HttpStatus} from '@nestjs/common';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {IEvent} from '#domain/entities/event.entity';
import {ITransactionWithLock} from '#domain/abstracts/relational-data-service/types';

export const ensureEventAvailable = async (
  rDataService: RelationalDataServiceAbstract,
  eventId: IEvent['id'],
  trx?: ITransactionWithLock,
): Promise<IEvent> => {
  const [event] = await rDataService.event.findById(eventId, trx);

  if (event) {
    return event;
  }

  const [deletedEvent] = await rDataService.deletedEvent.findByEventId(eventId, trx);

  if (deletedEvent) {
    throw new HttpException(
      {
        status: HttpStatus.GONE,
        error: `Event with id ${eventId} was deleted`,
      },
      HttpStatus.GONE,
    );
  }

  throw new HttpException(
    {
      status: HttpStatus.NOT_FOUND,
      error: `Event with id ${eventId} not found`,
    },
    HttpStatus.NOT_FOUND,
  );
};
