import {IDeletedEvent} from '#domain/entities/deleted-event.entity';
import {IQueryDetails, ITransaction, ITransactionWithLock} from '#domain/abstracts/relational-data-service/types';

export abstract class DeletedEventRepositoryAbstract {
  abstract findByEventId: (
    eventId: IDeletedEvent['eventId'],
    trx?: ITransactionWithLock,
  ) => Promise<[result: IDeletedEvent | null, queryDetails: IQueryDetails]>;

  abstract insert: (
    deletedEvent: IDeletedEvent,
    trx?: ITransaction,
  ) => Promise<[result: undefined, queryDetails: IQueryDetails]>;
}
