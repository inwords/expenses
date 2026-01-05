import {Injectable} from '@nestjs/common';
import {EventServiceAbstract} from '#domain/abstracts/event-service/event-service';
import {IEvent} from '#domain/entities/event.entity';
import {Result, success, error, isError} from '#packages/result';
import {EventNotFoundError, EventDeletedError, InvalidPinCodeError} from '#domain/errors/errors';

@Injectable()
export class EventService implements EventServiceAbstract {
  isValidEvent(
    event: IEvent | null | undefined,
    pinCode: string,
  ): Result<boolean, EventNotFoundError | EventDeletedError | InvalidPinCodeError> {
    const existsResult = this.isEventExists(event);

    if (isError(existsResult)) {
      return existsResult;
    }

    const notDeletedResult = this.isEventNotDeleted(event);

    if (isError(notDeletedResult)) {
      return notDeletedResult;
    }

    const pinCodeResult = this.isValidPinCode(event, pinCode);

    if (isError(pinCodeResult)) {
      return pinCodeResult;
    }

    return success(true);
  }

  isEventExists(event: IEvent | null | undefined): Result<boolean, EventNotFoundError> {
    if (!event) {
      return error(new EventNotFoundError());
    }

    return success(true);
  }

  isEventNotDeleted(event: IEvent): Result<boolean, EventDeletedError> {
    if (event.deletedAt !== null) {
      return error(new EventDeletedError());
    }

    return success(true);
  }

  isValidPinCode(event: IEvent, pinCode: string): Result<boolean, InvalidPinCodeError> {
    if (event.pinCode !== pinCode) {
      return error(new InvalidPinCodeError());
    }

    return success(true);
  }
}
