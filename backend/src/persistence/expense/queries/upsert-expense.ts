import {IUpsertExpense} from '../types';
import {Expense as ExpenseInterface} from '../../../domain/expense/types';
import {InjectEntityManager} from '@nestjs/typeorm';
import {EntityManager} from 'typeorm';
import {Injectable} from '@nestjs/common';
import {Expense} from '#persistence/entities/expense.entity';

@Injectable()
export class UpsertExpense implements IUpsertExpense {
  constructor(@InjectEntityManager() private readonly entityManager: EntityManager) {}

  public async execute(input: ExpenseInterface) {
    const entity = await this.entityManager.getRepository(Expense).save(input);

    return {...entity, createdAt: entity.createdAt.toISOString()};
  }
}
