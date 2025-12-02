import {HttpException, HttpStatus} from '@nestjs/common';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {IEvent} from '#domain/entities/event.entity';
import {ITransactionWithLock} from '#domain/abstracts/relational-data-service/types';

export const DELETION_GRACE_PERIOD_MS = 5 * 60 * 1000;

export const isDeletionGracePeriodElapsed = (deletedAt?: Date | null): boolean => {
  if (!deletedAt) {
    return false;
  }

  return Date.now() - new Date(deletedAt).getTime() >= DELETION_GRACE_PERIOD_MS;
};

export const ensureEventAvailable = async (
  rDataService: RelationalDataServiceAbstract,
  eventId: IEvent['id'],
  trx?: ITransactionWithLock,
  options: {allowPendingDeletion?: boolean} = {},
): Promise<IEvent> => {
  const [event] = await rDataService.event.findById(eventId, trx);

  if (event) {
    if (event.deletedAt && !options.allowPendingDeletion) {
      const isGraceElapsed = isDeletionGracePeriodElapsed(event.deletedAt);

      throw new HttpException(
        {
          status: HttpStatus.GONE,
          error: isGraceElapsed
            ? `Event with id ${eventId} is no longer available`
            : `Event with id ${eventId} is scheduled for deletion`,
        },
        HttpStatus.GONE,
      );
    }

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
