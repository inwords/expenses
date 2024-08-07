import {CreateExpense} from '@/entities/expense/types/types';
import {createExpense as createExpenseApi, getEventExpenses} from '@/entities/expense/services/api';
import {expenseStore} from '@/entities/expense/stores/expense-store';

export class ExpenseService {
  async createExpense(expense: CreateExpense) {
    const resp = await createExpenseApi(expense);

    expenseStore.setExpenses([...expenseStore.expenses, resp]);
  }

  async fetchExpenses(eventId: string) {
    const expenses = await getEventExpenses(eventId);

    expenseStore.setExpenses(expenses);
  }
}

export const expenseService = new ExpenseService();
