import {IQueryDetails, ITransaction, ITransactionWithLock} from '#domain/abstracts/relational-data-service/types';
import {ICurrency} from '#domain/entities/currency.entity';

export abstract class CurrencyRepositoryAbstract {
  abstract findById: (
    currencyId: ICurrency['id'],
    trx?: ITransactionWithLock,
  ) => Promise<[result: ICurrency | null, queryDetails: IQueryDetails]>;
  abstract findAll: (
    input: {
      limit: number;
    },
    trx?: ITransaction,
  ) => Promise<[result: ICurrency[], queryDetails: IQueryDetails]>;
  abstract insert: (
      user: ICurrency | ICurrency[],
      trx?: ITransaction,
  ) => Promise<[result: undefined, queryDetails: IQueryDetails]>;
}
