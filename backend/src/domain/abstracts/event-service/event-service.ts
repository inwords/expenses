import {IEvent} from '#domain/entities/event.entity';

export abstract class EventServiceAbstract {
  abstract validateEvent(event: IEvent | null | undefined, pinCode: string): asserts event is IEvent;
  abstract validateEventExists(event: IEvent | null | undefined): asserts event is IEvent;
  abstract validateEventIsNotDeleted(event: IEvent): void;
  abstract validatePinCode(event: IEvent, pinCode: string): void;
}
