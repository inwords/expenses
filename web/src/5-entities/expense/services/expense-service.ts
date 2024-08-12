import {CreateExpense, Tabs} from '@/5-entities/expense/types/types';
import {createExpense as createExpenseApi, getEventExpenses} from '@/5-entities/expense/services/api';
import {expenseStore} from '@/5-entities/expense/stores/expense-store';

export class ExpenseService {
  async createExpense(expense: CreateExpense) {
    const resp = await createExpenseApi(expense);

    expenseStore.setExpenses([...expenseStore.expenses, resp]);
  }

  async fetchExpenses(eventId: string) {
    const expenses = await getEventExpenses(eventId);

    expenseStore.setExpenses(expenses);
  }

  setSplitOption(splitOption: '1' | '2') {
    expenseStore.setSplitOption(splitOption);
  }

  setCurrentTab(currentTab: Tabs) {
    expenseStore.setCurrentTab(currentTab);
  }
}

export const expenseService = new ExpenseService();
