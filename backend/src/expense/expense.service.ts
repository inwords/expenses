import {Inject, Injectable} from '@nestjs/common';
import {InjectEntityManager} from '@nestjs/typeorm';
import {EntityManager} from 'typeorm';
import {Expense} from './expense.entity';
import {Event} from '../event/event.entity';
import {Currency} from '../currency/currency.entity';
import {CurrencyRateService} from '../currency-rate/currency-rate.service';
import {FindExpenses} from '../persistence/expense/queries/find-expenses';
import {SplitInfo} from '../domain/expense/types';

@Injectable()
export class ExpenseService {
  constructor(
    @InjectEntityManager() private readonly entityManager: EntityManager,
    private readonly currencyRateService: CurrencyRateService,
    @Inject(FindExpenses)
    private readonly findExpenses: FindExpenses,
  ) {}

  public getAllEventExpenses(eventId: number) {
    return this.findExpenses.execute({
      where: {
        eventId,
      },
    });
  }

  public async saveExpense(expense: Omit<Expense, 'id' | 'currency' | 'event' | 'user' | 'createdAt'>) {
    const eventRepository = this.entityManager.getRepository(Event);

    const currentEvent = await eventRepository.findOne({
      where: {
        id: expense.eventId,
      },
    });

    const expenseRepository = this.entityManager.getRepository(Expense);

    if (currentEvent.currencyId === expense.currencyId) {
      return expenseRepository.save(expense);
    } else {
      const currencyRepository = this.entityManager.getRepository(Currency);
      const expenseCurrencyCode = await currencyRepository.findOne({
        where: {
          id: expense.currencyId,
        },
        select: {
          code: true,
        },
      });
      const eventCurrencyCode = await currencyRepository.findOne({
        where: {
          id: currentEvent.currencyId,
        },
        select: {
          code: true,
        },
      });

      let splitInformation: SplitInfo[] = [];

      for (let i of expense.splitInformation) {
        splitInformation.push({
          ...i,
          amount: Number(
            Number(
              i.amount * (await this.currencyRateService.getRate(expenseCurrencyCode.code, eventCurrencyCode.code)),
            ).toFixed(2),
          ),
        });
      }

      return expenseRepository.save({...expense, splitInformation});
    }
  }
}
