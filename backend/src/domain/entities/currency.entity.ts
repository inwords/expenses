export enum CurrencyCode {
  EUR = 'EUR',
  USD = 'USD',
  RUB = 'RUB',
  JPY = 'JPY',
  TRY = 'TRY',
}

export interface ICurrency {
  id: string;
  code: CurrencyCode;
  createdAt: Date;
  updatedAt: Date;
}
