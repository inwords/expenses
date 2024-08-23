enum CurrencyCode {
  EUR = 'EUR',
  USD = 'USD',
  RUB = 'RUB',
  JPY = 'JPY',
  TRY = 'TRY',
}

export const CURRENCIES_ID_TO_CURRENCY_CODE: Record<string, CurrencyCode> = {
  '1': CurrencyCode.EUR,
  '2': CurrencyCode.USD,
  '3': CurrencyCode.RUB,
  '4': CurrencyCode.JPY,
  '5': CurrencyCode.TRY,
};
