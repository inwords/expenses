import {ValueObject} from '#domain/value-objects/value-object';
import {PartialByKeys} from '#packages/types';
import {IUser} from '#domain/entities/user.enitity';
import {ulid} from 'ulid';

export type TUserDefaultKeys = keyof Pick<IUser, 'id' | 'createdAt' | 'updatedAt'>;

export const userDefaultValues: {
  [K in TUserDefaultKeys]: () => IUser[K];
} = {
  id: () => ulid(),
  createdAt: () => new Date(),
  updatedAt: () => new Date(),
} as const;
Object.freeze(userDefaultValues);

export class UserValueObject extends ValueObject<IUser> {
  public override value: IUser;

  constructor(objectValues: PartialByKeys<IUser, TUserDefaultKeys>) {
    const valueObject: IUser = {
      // Class-validator устанавливает значение `undefined` для необязательных полей,
      // если они не переданы. Чтобы избежать перезаписи значений по-умолчанию на `undefined`,
      // сначала выполняется деструктуризация объекта requitedObjectValues.
      ...objectValues,
      id: ValueObject.getValueOrDefault(objectValues.id, userDefaultValues.id),
      createdAt: ValueObject.getValueOrDefault(objectValues.createdAt, userDefaultValues.createdAt),
      updatedAt: ValueObject.getValueOrDefault(objectValues.updatedAt, userDefaultValues.updatedAt),
    };
    super(valueObject);
    this.value = valueObject;
  }
}
