import {ValueObject} from '#domain/value-objects/value-object';
import {IEvent} from '#domain/entities/event.entity';
import {PartialByKeys} from '#packages/types';
import {ulid} from 'ulid';

export type TEventDefaultKeys = keyof Pick<IEvent, 'id' | 'createdAt' | 'updatedAt'>;

export const eventDefaultValues: {
  [K in TEventDefaultKeys]: () => IEvent[K];
} = {
  id: () => ulid(),
  createdAt: () => new Date(),
  updatedAt: () => new Date(),
} as const;
Object.freeze(eventDefaultValues);

export class EventValueObject extends ValueObject<IEvent> {
  public override value: IEvent;

  constructor(objectValues: PartialByKeys<IEvent, TEventDefaultKeys>) {
    const valueObject: IEvent = {
      // Class-validator устанавливает значение `undefined` для необязательных полей,
      // если они не переданы. Чтобы избежать перезаписи значений по-умолчанию на `undefined`,
      // сначала выполняется деструктуризация объекта requitedObjectValues.
      ...objectValues,
      id: ValueObject.getValueOrDefault(objectValues.id, eventDefaultValues.id),
      createdAt: ValueObject.getValueOrDefault(objectValues.createdAt, eventDefaultValues.createdAt),
      updatedAt: ValueObject.getValueOrDefault(objectValues.updatedAt, eventDefaultValues.updatedAt),
    };
    super(valueObject);
    this.value = valueObject;
  }
}
