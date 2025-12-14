import {IUserInfo} from '#domain/entities/user-info.entity';
import {IQueryDetails, ITransaction, ITransactionWithLock} from '#domain/abstracts/relational-data-service/types';

export abstract class UserInfoRepositoryAbstract {
  abstract findByEventId: (
    eventId: IUserInfo['eventId'],
    trx?: ITransactionWithLock,
  ) => Promise<[result: IUserInfo[], queryDetails: IQueryDetails]>;
  abstract insert: (
    userInfo: IUserInfo | IUserInfo[],
    trx?: ITransaction,
  ) => Promise<[result: undefined, queryDetails: IQueryDetails]>;
}
