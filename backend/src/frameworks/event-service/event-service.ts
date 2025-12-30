import {Injectable} from '@nestjs/common';
import {EventServiceAbstract} from '#domain/abstracts/event-service/event-service';
import {IEvent} from '#domain/entities/event.entity';
import {BusinessError} from '#domain/errors/business.error';
import {BUSINESS_ERRORS} from '#domain/errors/business-errors.const';
import {ErrorCode} from '#domain/errors';

@Injectable()
export class EventService implements EventServiceAbstract {
  validateEvent(event: IEvent | null | undefined, pinCode: string): asserts event is IEvent {
    this.validateEventExists(event);
    this.validatePinCode(event, pinCode);
  }

  validateEventExists(event: IEvent | null | undefined): asserts event is IEvent {
    if (!event) {
      throw new BusinessError(BUSINESS_ERRORS[ErrorCode.EVENT_NOT_FOUND], {eventId: event?.id});
    }
  }

  validateEventIsNotDeleted(event: IEvent): void {
    if (event.deletedAt !== null) {
      throw new BusinessError(BUSINESS_ERRORS[ErrorCode.EVENT_ALREADY_DELETED], {eventId: event.id});
    }
  }

  validatePinCode(event: IEvent, pinCode: string): void {
    if (event.pinCode !== pinCode) {
      throw new BusinessError(BUSINESS_ERRORS[ErrorCode.EVENT_INVALID_PIN], {eventId: event.id});
    }
  }
}
