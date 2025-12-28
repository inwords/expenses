import {IEvent} from '#domain/entities/event.entity';

export abstract class EventServiceAbstract {
  abstract validateEventExists(event: IEvent | null | undefined): asserts event is IEvent;
  abstract validateEventIsNotDeleted(event: IEvent): void;
  abstract validatePinCode(event: IEvent, pinCode: string): void;
}
