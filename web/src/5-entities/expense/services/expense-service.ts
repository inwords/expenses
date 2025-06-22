import {
  CreateExpenseForm,
  CreateExpenseRefundForm,
  Expense,
  ExpenseRefund,
  Tabs
} from '@/5-entities/expense/types/types';
import {createExpense as createExpenseApi, getEventExpenses} from '@/5-entities/expense/services/api';
import {expenseStore} from '@/5-entities/expense/stores/expense-store';
import {ExpenseType} from '@/5-entities/expense/constants';
import {userStore} from '@/5-entities/user/stores/user-store';

export class ExpenseService {
  async createExpense(data: CreateExpenseForm, id: string) {
    const {amount, splitOption, ...rest} = data;

    const body = {
      ...rest,
      eventId: id,
      splitInformation:
        expenseStore.splitOption === '1'
          ? userStore.users.map((u) => {
              return {userId: u.id, amount: Number(data.amount) / userStore.users.length};
            })
          : data.splitInformation.map((i) => {
              return {...i, amount: Number(i.amount)};
            }),
    };
    const resp = await createExpenseApi({...body, expenseType: ExpenseType.Expense});

    expenseStore.setExpenses([...expenseStore.expenses, resp]);
  }

  async fetchExpenses(eventId: string) {
    const expenses = await getEventExpenses(eventId);

    expenseStore.setExpenses(expenses.filter((e: Expense | ExpenseRefund) => e.expenseType === ExpenseType.Expense));
    expenseStore.setExpenseRefunds(expenses.filter((e: Expense | ExpenseRefund) => e.expenseType === ExpenseType.Refund));
  }

  async createExpenseRefund(expenseRefund: CreateExpenseRefundForm) {
    const {userWhoReceiveId, amount, ...rest} = expenseRefund;

    const resp = await createExpenseApi({
      ...rest,
      expenseType: ExpenseType.Refund,
      splitInformation: [{userId: userWhoReceiveId, amount}],
    });

    expenseStore.setExpenseRefunds([...expenseStore.expenseRefunds, resp]);
  }

  setSplitOption(splitOption: '1' | '2') {
    expenseStore.setSplitOption(splitOption);
  }

  setCurrentTab(currentTab: Tabs) {
    expenseStore.setCurrentTab(currentTab);
  }
}

export const expenseService = new ExpenseService();
