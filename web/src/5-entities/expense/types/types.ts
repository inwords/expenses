import {ExpenseType} from '@/5-entities/expense/constants';

export interface CreateExpenseForm {
  description: string;
  userWhoPaidId: number;
  currencyId: number;
  eventId: number;
  splitInformation: Array<Omit<SplitInfo, 'exchangedAmount'>>;
  amount: number;
  splitOption: string;
}

export interface CreateExpenseRefundForm {
  description: string;
  userWhoPaidId: number;
  currencyId: number;
  eventId: number;
  amount: number;
  userWhoReceiveId: number;
}

export interface CreateExpense extends Omit<CreateExpenseForm, 'amount' | 'splitOption'> {
  expenseType: ExpenseType;
}

export interface SplitInfo {
  userId: number;
  amount: number;
  exchangedAmount: number;
}

export interface ExpenseBase {
  id: string;
  description: string;
  userWhoPaidId: number;
  currencyId: number;
  eventId: number;
  splitInformation: Array<SplitInfo>;
  createdAt: string;
}

export interface Expense extends ExpenseBase {
  expenseType: ExpenseType.Expense;
}

export interface ExpenseRefund extends ExpenseBase {
  expenseType: ExpenseType.Refund;
}

export type Tabs = 0 | 1 | 2 | 3 | 4;
