import {IUpsertExpense} from '../types';
import {Expense as ExpenseInterface} from '../../../domain/expense/types';
import {InjectEntityManager} from '@nestjs/typeorm';
import {EntityManager} from 'typeorm';
import {Expense} from '../../../expense/expense.entity';
import {Injectable} from '@nestjs/common';

@Injectable()
export class UpsertExpense implements IUpsertExpense {
  constructor(@InjectEntityManager() private readonly entityManager: EntityManager) {}

  public async execute(input: ExpenseInterface) {
    const entity = await this.entityManager.getRepository(Expense).save(input);

    return {...entity, createdAt: entity.createdAt.toISOString()};
  }
}
