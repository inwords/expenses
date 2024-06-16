import {Injectable} from '@nestjs/common';
import {InjectEntityManager} from '@nestjs/typeorm';
import {EntityManager} from 'typeorm';
import {Currency} from './currency.entity';

@Injectable()
export class CurrencyService {
  constructor(@InjectEntityManager() private readonly entityManager: EntityManager) {}

  public async getAllCurrencies() {
    return this.entityManager.getRepository(Currency).find();
  }
}
