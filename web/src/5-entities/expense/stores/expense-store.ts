import {CreateExpenseRefundForm, Expense, ExpenseRefund, Tabs} from '@/5-entities/expense/types/types';
import {makeAutoObservable} from 'mobx';
import {userStore} from '@/5-entities/user/stores/user-store';

export class ExpenseStore {
  expenses: Array<Expense> = [];
  expenseRefunds: Array<ExpenseRefund> = [];
  splitOption: '1' | '2' = '1';
  currentTab: Tabs = 0;
  isExpenseRefundModalOpen: boolean = false;
  currentExpenseRefund: Partial<CreateExpenseRefundForm> = {};

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

  get expenseRefundsToView() {
    return this.expenseRefunds.map((expense) => {
      return {...expense, amount: expense.splitInformation.reduce((prev, info) => prev + info.amount, 0)};
    });
  }

  get currentUserExpenseRefunds() {
    return this.expenseRefundsToView.filter((e) => {
      return e.splitInformation.some((i) => i.userId === userStore.currentUser?.id);
    });
  }

  get currentUserDebts() {
    const debts = this.expenses.reduce<Record<string, number>>((prev, curr) => {
      if (curr.userWhoPaidId !== userStore.currentUser?.id) {

        const userName = userStore.usersDictIdToName[curr.userWhoPaidId];

        prev[userName] =
          (prev[userName] || 0) +
          curr.splitInformation.reduce((pre, cur) => {
            if (cur.userId === userStore.currentUser?.id) {
              pre += cur.amount;
            }

            return pre;
          }, 0);
      }

      return prev;
    }, {});

    this.expenseRefunds.forEach((r) => {
      if (userStore.currentUser?.id === r.userWhoPaidId) {
        r.splitInformation.forEach((i) => {
          const userName = userStore.usersDictIdToName[i.userId];

          if (debts[userName]) {
            debts[userName] -= i.amount;
          }
        });
      }
    });

    return debts;
  }

  setExpenses(expenses: Array<Expense>) {
    this.expenses = expenses;
  }

  setExpenseRefunds(expenseRefunds: Array<ExpenseRefund>) {
    this.expenseRefunds = expenseRefunds;
  }

  setSplitOption(splitOption: '1' | '2') {
    this.splitOption = splitOption;
  }

  setCurrentTab(currentTab: Tabs) {
    this.currentTab = currentTab;
  }

  setIsExpenseRefundModalOpen(isExpenseRefundModalOpen: boolean) {
    this.isExpenseRefundModalOpen = isExpenseRefundModalOpen;
  }

  setCurrentExpenseRefund(expenseRefund: Partial<CreateExpenseRefundForm>) {
    this.currentExpenseRefund = expenseRefund;
  }
}

export const expenseStore = new ExpenseStore();
