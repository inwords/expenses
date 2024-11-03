import {IFIndCurrencies} from '../types';
import {InjectEntityManager} from '@nestjs/typeorm';
import {EntityManager} from 'typeorm';
import {Currency} from "#persistence/entities/currency.entity";
import {Injectable} from "@nestjs/common";

@Injectable()
export class FindCurrencies implements IFIndCurrencies {
  constructor(@InjectEntityManager() private readonly entityManager: EntityManager) {}

  public async execute() {
    return await this.entityManager.getRepository(Currency).find();
  }
}
