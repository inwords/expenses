import {IQueryDetails, ITransaction, ITransactionWithLock} from '#domain/abstracts/relational-data-service/types';
import {ICurrencyRate} from '#domain/entities/currency-rate.entity';

export abstract class CurrencyRateRepositoryAbstract {
  abstract findByDate: (
    date: string,
    trx?: ITransactionWithLock,
  ) => Promise<[result: ICurrencyRate | null, queryDetails: IQueryDetails]>;
  abstract insert: (
    currencyRate: ICurrencyRate,
    trx?: ITransaction,
  ) => Promise<[result: undefined, queryDetails: IQueryDetails]>;
}
