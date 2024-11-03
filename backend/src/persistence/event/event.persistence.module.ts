import {Module} from '@nestjs/common';
import {FindEvent} from './queries/find-event';
import {UpsertEvent} from "./queries/upsert-event";

const queries = [FindEvent, UpsertEvent];

@Module({
  providers: [...queries],
  exports: [...queries],
})
export class EventPersistenceModule {}
