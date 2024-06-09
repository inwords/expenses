import {Body, Controller, HttpCode, Post} from '@nestjs/common';
import {EVENT_ROUTES} from './constants';
import {EventService} from './event.service';
import {CrateEventBodyDto} from './dto/event';
import {ulid} from 'ulid';

@Controller(EVENT_ROUTES)
export class EventController {
  constructor(private readonly eventService: EventService) {}
  @Post('/')
  @HttpCode(201)
  createEvent(@Body() body: CrateEventBodyDto) {
    const mappedUsers = body.users.map((user) => {
      return {...user, id: ulid()};
    });

    return this.eventService.saveEvent({...body, users: mappedUsers});
  }
}
