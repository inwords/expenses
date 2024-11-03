import {IUpsertCurrencyRate, UpsertCurrencyRateInput} from '../types';
import {InjectEntityManager} from '@nestjs/typeorm';
import {EntityManager} from 'typeorm';
import {CurrencyRate} from "#persistence/entities/currency-rate.entity";
import {Injectable} from "@nestjs/common";

@Injectable()
export class UpsertCurrencyRate implements IUpsertCurrencyRate {
  constructor(@InjectEntityManager() private readonly entityManager: EntityManager) {}

  public async execute(input: UpsertCurrencyRateInput) {
    const currencyRateRepository = this.entityManager.getRepository(CurrencyRate);

    await currencyRateRepository.save(input);
  }
}
