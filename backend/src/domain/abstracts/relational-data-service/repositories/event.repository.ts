import {IEvent} from '#domain/entities/event.entity';
import {IQueryDetails, ITransaction, ITransactionWithLock} from '#domain/abstracts/relational-data-service/types';

export abstract class EventRepositoryAbstract {
  abstract findById: (
    id: IEvent['id'],
    trx?: ITransactionWithLock,
  ) => Promise<[result: IEvent | null, queryDetails: IQueryDetails]>;
  abstract insert: (event: IEvent, trx?: ITransaction) => Promise<[result: undefined, queryDetails: IQueryDetails]>;
}
