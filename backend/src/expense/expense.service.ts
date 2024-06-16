import {Injectable} from '@nestjs/common';
import {InjectEntityManager} from '@nestjs/typeorm';
import {EntityManager} from 'typeorm';
import {Expense} from './expense.entity';

@Injectable()
export class ExpenseService {
  constructor(@InjectEntityManager() private readonly entityManager: EntityManager) {}

  public getAllEventExpenses(eventId: number) {
    return this.entityManager.getRepository(Expense).find({
      where: {
        eventId,
      },
    });
  }

  public saveExpense(expense: Omit<Expense, 'id' | 'currency' | 'event' | 'user' | 'createdAt'>) {
    return this.entityManager.getRepository(Expense).save(expense);
  }
}
