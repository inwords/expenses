import {Expense} from '@/5-entities/expense/types/types';
import {action, computed, makeObservable, observable} from 'mobx';

export class ExpenseStore {
  expenses: Array<Expense> = [];
  splitOption: '1' | '2' = '1';

  constructor() {
    makeObservable(this, {
      expenses: observable,
      splitOption: observable,
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

  setSplitOption( splitOption: '1' | '2') {
    this.splitOption = splitOption;
  }
}

export const expenseStore = new ExpenseStore();
