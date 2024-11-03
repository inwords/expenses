import {ExpenseType} from './constants';
import {DateIsoString} from '#packages/types';

export interface Expense {
  id: number;
  description: string;
  userWhoPaidId: number;
  currencyId: number;
  eventId: number;
  expenseType: ExpenseType;
  splitInformation: Array<SplitInfo>;
  createdAt: DateIsoString;
}

export interface SplitInfo {
  userId: string;
  amount: number;
}
