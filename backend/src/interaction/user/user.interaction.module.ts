import {Module} from '@nestjs/common';
import {EventPersistenceModule} from '../../persistence/event/event.persistence.module';
import {UserPersistenceModule} from "../../persistence/user/user.persistence.module";
import {SaveUsersToEvent} from "./use-cases/save-users-to-event";

const useCases = [SaveUsersToEvent];

@Module({
  imports: [EventPersistenceModule, UserPersistenceModule],
  providers: [...useCases],
  exports: [...useCases],
})
export class UserInteractionModule {}
