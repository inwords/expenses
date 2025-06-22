import {ICurrencyRate} from '#domain/entities/currency-rate.entity';

export abstract class CurrencyRateServiceAbstract {
  abstract getCurrencyRate: (date: ICurrencyRate['date']) => Promise<Record<string, number> | null>;
}
