import {IQueryDetails, ITransaction, ITransactionWithLock} from '#domain/abstracts/relational-data-service/types';
import {IExpense} from '#domain/entities/expense.entity';

export abstract class ExpenseRepositoryAbstract {
  abstract findByEventId: (
    eventId: IExpense['eventId'],
    trx?: ITransactionWithLock,
  ) => Promise<[result: IExpense[], queryDetails: IQueryDetails]>;
  abstract insert: (expense: IExpense, trx?: ITransaction) => Promise<[result: undefined, queryDetails: IQueryDetails]>;
}
