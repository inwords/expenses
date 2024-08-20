import {Body, Controller, Delete, Get, HttpCode, Param, Post, Query} from '@nestjs/common';
import {EVENT_ROUTES} from './constants';
import {EventService} from './event.service';
import {AddUsersToEventDto, CrateEventBodyDto, DeleteUserQuery, EventIdDto, GetEventInfoQueryDto} from './dto/event';

@Controller(EVENT_ROUTES.root)
export class EventController {
  constructor(private readonly eventService: EventService) {}
  @Post(EVENT_ROUTES.event)
  @HttpCode(201)
  async createEvent(@Body() body: CrateEventBodyDto) {
    return this.eventService.saveEvent(body);
  }

  @Get(EVENT_ROUTES.getEventInfo)
  async getEventInfo(@Param() {eventId}: EventIdDto, @Query() query: GetEventInfoQueryDto) {
    return this.eventService.getEventInfo(eventId, query.pinCode);
  }

  @Post(EVENT_ROUTES.addUsersToEvent)
  @HttpCode(201)
  async addUserToEvent(@Param() {eventId}: EventIdDto, @Body() body: AddUsersToEventDto) {
    return this.eventService.addUsersToEvent(eventId, body.users, body.pinCode);
  }

  @Delete(EVENT_ROUTES.deleteUser)
  @HttpCode(200)
  async deleteUser(@Query() query: DeleteUserQuery) {
    return this.eventService.deleteUserFromEvent(query.pinCode, query.userId, query.eventId);
  }
}
