import {FindCurrencyInput, IFindCurrency} from '../types';
import {InjectEntityManager} from '@nestjs/typeorm';
import {EntityManager} from 'typeorm';
import {Currency} from '#persistence/entities/currency.entity';
import {Injectable} from "@nestjs/common";

@Injectable()
export class FindCurrency implements IFindCurrency {
  constructor(@InjectEntityManager() private readonly entityManager: EntityManager) {}

  public async execute({currencyId}: FindCurrencyInput) {
    const currency = await this.entityManager.getRepository(Currency).findOne({
      where: {
        id: currencyId,
      },
    });

    return currency;
  }
}
