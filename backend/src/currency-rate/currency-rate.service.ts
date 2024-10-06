import {InjectEntityManager} from '@nestjs/typeorm';
import {EntityManager} from 'typeorm';
import {CurrencyRate} from './currency-rate.entity';
import {getCurrentDateWithoutTime} from '../packages/date-utils';
import {CurrencyCode} from '../currency/currency.entity';

export class CurrencyRateService {
  constructor(@InjectEntityManager() private readonly entityManager: EntityManager) {}
  async setCurrenciesChangeRates(rates: Record<string, number>) {
    const currencyRateRepository = this.entityManager.getRepository(CurrencyRate);

    await currencyRateRepository.save({date: getCurrentDateWithoutTime(), rate: rates});
  }
  public async getRate(from: CurrencyCode, to: CurrencyCode) {
    const currencyRateRepository = await this.entityManager.getRepository(CurrencyRate);
    const rates: any = await currencyRateRepository.findOne({
      where: {
        date: getCurrentDateWithoutTime(),
      },
    });

    return rates.rate[to] / rates.rate[from];
  }
}
