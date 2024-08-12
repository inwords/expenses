import {Expense, Tabs} from '@/5-entities/expense/types/types';
import {makeAutoObservable} from 'mobx';
import {userStore} from '@/5-entities/user/stores/user-store';

export class ExpenseStore {
  expenses: Array<Expense> = [];
  splitOption: '1' | '2' = '1';
  currentTab: Tabs = 0;

  constructor() {
    makeAutoObservable(this);
  }

  get expensesToView() {
    return this.expenses.map((expense) => {
      return {...expense, amount: expense.splitInformation.reduce((prev, info) => prev + info.amount, 0)};
    });
  }

  get currentUserExpenses() {
    return this.expensesToView.filter((e) => {
      return e.splitInformation.some((i) => Number(i.userId) === userStore.currentUser?.id);
    });
  }

  setExpenses(expenses: Array<Expense>) {
    this.expenses = expenses;
  }

  setSplitOption(splitOption: '1' | '2') {
    this.splitOption = splitOption;
  }

  setCurrentTab(currentTab: Tabs) {
    this.currentTab = currentTab;
  }
}

export const expenseStore = new ExpenseStore();
