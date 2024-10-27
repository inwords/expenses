import {Module} from '@nestjs/common';
import {FindEvent} from './queries/find-event';

const queries = [FindEvent];

@Module({
  providers: [...queries],
  exports: [...queries],
})
export class EventPersistenceModule {}
