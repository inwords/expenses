import {Module} from '@nestjs/common';
import {EventController} from './event.controller';
import {EventService} from './event.service';
import {UserModule} from '../user/user.module';

@Module({
  controllers: [EventController],
  providers: [EventService],
  imports: [UserModule],
})
export class EventModule {}
