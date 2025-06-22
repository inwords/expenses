type ValueObjectProps = Record<string, any>;

export abstract class ValueObject<T extends ValueObjectProps> {
  public readonly value!: T;

  constructor(valueObject: Partial<T>) {
    Object.freeze(valueObject);
  }

  // Статический метод для проверки значения на undefined и применения значения по умолчанию
  protected static getValueOrDefault<T>(value: T | undefined, getDefault: () => T): T {
    return typeof value !== 'undefined' ? value : getDefault();
  }
}
