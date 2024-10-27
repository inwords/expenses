import {FindCurrencyRateInput, IFindCurrencyRate} from '../types';
import {InjectEntityManager} from '@nestjs/typeorm';
import {EntityManager} from 'typeorm';
import {CurrencyRate} from '../../../currency-rate/currency-rate.entity';

export class FindCurrencyRate implements IFindCurrencyRate {
  constructor(@InjectEntityManager() private readonly entityManager: EntityManager) {}

  public async execute({date}: FindCurrencyRateInput) {
    const currency = await this.entityManager.getRepository(CurrencyRate).findOne({
      where: {
        date,
      },
    });

    return currency;
  }
}
