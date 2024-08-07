import {Expense} from '@/entities/expense/types/types';
import {action, computed, makeObservable, observable} from 'mobx';

export class ExpenseStore {
  expenses: Array<Expense> = [];

  constructor() {
    makeObservable(this, {
      expenses: observable,
      expensesToView: computed,
      setExpenses: action,
    });
  }

  get expensesToView() {
    return this.expenses.map((expense) => {
      return {...expense, amount: expense.splitInformation.reduce((prev, info) => prev + info.amount, 0)};
    });
  }

  setExpenses(expenses: Array<Expense>) {
    this.expenses = expenses;
  }
}

export const expenseStore = new ExpenseStore();
