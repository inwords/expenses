import {EntityManager} from 'typeorm';
import {FindManyOptions} from 'typeorm/find-options/FindManyOptions';
import {InjectEntityManager} from '@nestjs/typeorm';
import {Injectable} from '@nestjs/common';
import {FindExpensesInput} from '../types';
import {Expense} from '#persistence/entities/expense.entity';

@Injectable()
export class FindExpenses implements FindExpenses {
  constructor(@InjectEntityManager() private readonly entityManager: EntityManager) {}
  public async execute(input: FindManyOptions<FindExpensesInput>) {
    const expenses = await this.entityManager.getRepository(Expense).find(input);

    return expenses.map((e) => {
      return {
        ...e,
        createdAt: e.createdAt.toISOString(),
      };
    });
  }
}
