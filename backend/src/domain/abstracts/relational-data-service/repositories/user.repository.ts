import {IUser} from '#domain/entities/user.enitity';
import {IQueryDetails, ITransaction, ITransactionWithLock} from '#domain/abstracts/relational-data-service/types';

export abstract class UserRepositoryAbstract {
  abstract findByEventId: (
    eventId: IUser['eventId'],
    trx?: ITransactionWithLock,
  ) => Promise<[result: IUser[], queryDetails: IQueryDetails]>;
  abstract insert: (
    user: IUser | IUser[],
    trx?: ITransaction,
  ) => Promise<[result: undefined, queryDetails: IQueryDetails]>;
}
