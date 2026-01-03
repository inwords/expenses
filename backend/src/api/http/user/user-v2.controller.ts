import {Body, Controller, HttpCode, Param, Post} from '@nestjs/common';
import {UserV2Routes} from './user.contants';
import {ApiTags} from '@nestjs/swagger';

import {EventIdDto} from './dto/event-id.dto';
import {AddUsersToEventDto} from './dto/add-users-to-event.dto';
import {GetEventInfoBodyDto} from './dto/get-event-info-body.dto';
import {GetEventExpensesBodyDto} from './dto/get-event-expenses-body.dto';
import {CreateExpenseV2Dto} from './dto/create-expense-v2.dto';
import {CreateShareTokenBodyDto} from './dto/create-share-token-body.dto';

import {
  GetEventInfoV2UseCase,
  SaveUsersToEventV2UseCase,
  SaveEventExpenseV2UseCase,
  GetEventExpensesV2UseCase,
  CreateEventShareTokenV2UseCase,
} from '#usecases/users/v2';

@Controller(UserV2Routes.root)
@ApiTags('User V2')
export class UserV2Controller {
  constructor(
    private readonly getEventInfoV2UseCase: GetEventInfoV2UseCase,
    private readonly saveUsersToEventV2UseCase: SaveUsersToEventV2UseCase,
    private readonly saveEventExpenseV2UseCase: SaveEventExpenseV2UseCase,
    private readonly getEventExpensesV2UseCase: GetEventExpensesV2UseCase,
    private readonly createEventShareTokenV2UseCase: CreateEventShareTokenV2UseCase,
  ) {}

  @Post(UserV2Routes.getEventInfo)
  async getEventInfo(@Param() {eventId}: EventIdDto, @Body() body: GetEventInfoBodyDto) {
    return this.getEventInfoV2UseCase.execute({eventId, pinCode: body.pinCode, token: body.token});
  }

  @Post(UserV2Routes.addUsersToEvent)
  @HttpCode(201)
  async addUserToEvent(@Param() {eventId}: EventIdDto, @Body() body: AddUsersToEventDto) {
    return this.saveUsersToEventV2UseCase.execute({eventId, ...body});
  }

  @Post(UserV2Routes.getAllEventExpenses)
  async getAllEventExpenses(@Param() {eventId}: EventIdDto, @Body() body: GetEventExpensesBodyDto) {
    return this.getEventExpensesV2UseCase.execute({eventId, pinCode: body.pinCode});
  }

  @Post(UserV2Routes.createExpense)
  async createExpense(@Body() expense: CreateExpenseV2Dto, @Param() {eventId}: EventIdDto) {
    return this.saveEventExpenseV2UseCase.execute({...expense, eventId});
  }

  @Post(UserV2Routes.createShareToken)
  @HttpCode(201)
  async createShareToken(@Param() {eventId}: EventIdDto, @Body() body: CreateShareTokenBodyDto) {
    return this.createEventShareTokenV2UseCase.execute({eventId, pinCode: body.pinCode});
  }
}
