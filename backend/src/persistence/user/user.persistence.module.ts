import {Module} from '@nestjs/common';
import {UpsertUsers} from './queries/upsert-users';
import {FindUsers} from './queries/find-users';

const queries = [UpsertUsers, FindUsers];

@Module({
  providers: [...queries],
  exports: [...queries],
})
export class UserPersistenceModule {}
