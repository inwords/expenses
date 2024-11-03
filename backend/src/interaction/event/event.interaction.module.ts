import {Module} from '@nestjs/common';
import {SaveEvent} from './use-cases/save-event';
import {UserPersistenceModule} from '../../persistence/user/user.persistence.module';
import {EventPersistenceModule} from '../../persistence/event/event.persistence.module';
import {GetEventInfo} from './use-cases/get-event-info';

const useCases = [SaveEvent, GetEventInfo];

@Module({
  imports: [UserPersistenceModule, EventPersistenceModule],
  providers: [...useCases],
  exports: [...useCases],
})
export class EventInteractionModule {}
