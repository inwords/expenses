import {Module} from '@nestjs/common';
import {EventService} from './event.service';
import {UserModule} from '../user/user.module';

@Module({
  exports: [EventService],
  providers: [EventService],
  imports: [UserModule],
})
export class EventModule {}
