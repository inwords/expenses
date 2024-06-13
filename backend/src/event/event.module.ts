import {Module} from '@nestjs/common';
import {EventController} from './event.controller';
import {EventService} from './event.service';
import {HashingModule} from "../hashing/hashing.module";

@Module({
  controllers: [EventController],
  providers: [EventService],
  imports: [HashingModule],
})
export class EventModule {}
