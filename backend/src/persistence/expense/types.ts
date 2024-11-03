import {Query} from '#packages/query';
import {FindManyOptions} from 'typeorm/find-options/FindManyOptions';
import {Expense as ExpenseInterface} from '#domain/expense/types';

export type FindExpensesInput = Pick<ExpenseInterface, 'eventId'>;
export type IFindExpenses = Query<FindManyOptions<FindExpensesInput>, Array<ExpenseInterface>>;

export type UpsertExpenseInput = Omit<ExpenseInterface, 'id'> & Partial<Pick<ExpenseInterface, 'id'>>;
export type IUpsertExpense = Query<UpsertExpenseInput, ExpenseInterface>;
