import {CurrencyCode, ICurrency} from '#domain/entities/currency.entity';

export const CURRENCIES_LIST: Pick<ICurrency, 'code'>[] = [
  {code: CurrencyCode.EUR},
  {code: CurrencyCode.USD},
  {code: CurrencyCode.RUB},
  {code: CurrencyCode.JPY},
  {code: CurrencyCode.TRY},
  {code: CurrencyCode.AED},
];
