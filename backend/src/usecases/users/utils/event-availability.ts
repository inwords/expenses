import {HttpException, HttpStatus} from '@nestjs/common';
import {QueryFailedError} from 'typeorm';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {IEvent} from '#domain/entities/event.entity';
import {ITransactionWithLock} from '#domain/abstracts/relational-data-service/types';

export const ensureEventAvailable = async (
  rDataService: RelationalDataServiceAbstract,
  eventId: IEvent['id'],
  pinCode?: IEvent['pinCode'],
  trx?: ITransactionWithLock,
): Promise<IEvent> => {
  const hasLock = trx?.lock !== undefined;

  // If pinCode is provided, check including deleted events (to distinguish 404 vs 410)
  // If pinCode is not provided, only check non-deleted events (deleted appears as 404)
  const query = pinCode !== undefined
    ? () => rDataService.event.findByIdIncludingDeleted(eventId, trx)
    : () => rDataService.event.findById(eventId, trx);

  let event: IEvent | null;
  if (hasLock) {
    event = await runWithLockHandling(query, eventId);
  } else {
    [event] = await query();
  }

  // 404: Event not found (or deleted when pinCode not provided)
  if (!event) {
    throw new HttpException(
      {
        status: HttpStatus.NOT_FOUND,
        error: `Event with id ${eventId} not found`,
      },
      HttpStatus.NOT_FOUND,
    );
  }

  // 403: Invalid pin code (only checked when pinCode is provided)
  if (pinCode !== undefined && event.pinCode !== pinCode) {
    throw new HttpException(
      {
        status: HttpStatus.FORBIDDEN,
        error: 'Invalid event pin code',
      },
      HttpStatus.FORBIDDEN,
    );
  }

  // 410: Event was deleted (only revealed after pin code validation)
  if (event.deletedAt) {
    throw new HttpException(
      {
        status: HttpStatus.GONE,
        error: `Event with id ${eventId} was deleted`,
      },
      HttpStatus.GONE,
    );
  }

  return event;
};

const runWithLockHandling = async <TResult, TQueryDetails>(
  query: () => Promise<[TResult, TQueryDetails]>,
  eventId: IEvent['id'],
): Promise<TResult> => {
  try {
    const [result] = await query();
    return result;
  } catch (error) {
    if (isLockError(error)) {
      throw new HttpException(
        {
          status: HttpStatus.CONFLICT,
          error: `Event with id ${eventId} is currently being modified, please retry`,
        },
        HttpStatus.CONFLICT,
      );
    }

    throw error;
  }
};

const isLockError = (error: unknown): error is QueryFailedError => {
  if (!(error instanceof QueryFailedError)) return false;
  const driverError = error.driverError;
  if (!driverError || typeof driverError !== 'object') return false;
  const code = driverError.code;
  return typeof code === 'string' && code === '55P03';
};
