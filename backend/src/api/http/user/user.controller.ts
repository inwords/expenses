import {Body, Controller, Get, HttpCode, Param, Post, Query} from '@nestjs/common';
import {UserRoutes} from './user.contants';
import {ApiTags} from '@nestjs/swagger';

import {CrateEventBodyDto} from './dto/create-event.dto';
import {GetEventInfoQueryDto} from './dto/get-event-info.dto';
import {EventIdDto} from './dto/event-id.dto';
import {AddUsersToEventDto} from './dto/add-users-to-event.dto';
import {CreatedExpenseDto} from './dto/create-expense.dto';
import {GetEventExpenses} from '../../../interaction/expense/use-cases/get-event-expenses';
import {SaveEventExpense} from '../../../interaction/expense/use-cases/save-event-expense';
import {GetAllCurrencies} from '../../../interaction/currency/use-cases/get-all-currencies';
import {SaveEvent} from '../../../interaction/event/use-cases/save-event';
import {GetEventInfo} from '../../../interaction/event/use-cases/get-event-info';
import {SaveUsersToEvent} from '../../../interaction/user/use-cases/save-users-to-event';

@Controller(UserRoutes.root)
@ApiTags('User')
export class UserController {
  constructor(
    private readonly getEventExpensesUseCase: GetEventExpenses,
    private readonly saveEventExpenseUseCase: SaveEventExpense,
    private readonly getAllCurrenciesUseCase: GetAllCurrencies,
    private readonly saveEventUseCase: SaveEvent,
    private readonly getEventInfoUseCase: GetEventInfo,
    private readonly saveUsersToEventUseCase: SaveUsersToEvent,
  ) {}
  @Get(UserRoutes.getAllCurrencies)
  @HttpCode(200)
  async getAllCurrencies() {
    return this.getAllCurrenciesUseCase.execute();
  }

  @Post(UserRoutes.createEvent)
  @HttpCode(201)
  async createEvent(@Body() body: CrateEventBodyDto) {
    const {users, ...event} = body;

    return this.saveEventUseCase.execute({users, event});
  }

  @Get(UserRoutes.getEventInfo)
  async getEventInfo(@Param() {eventId}: EventIdDto, @Query() query: GetEventInfoQueryDto) {
    return this.getEventInfoUseCase.execute({eventId, pinCode: query.pinCode});
  }

  @Post(UserRoutes.addUsersToEvent)
  @HttpCode(201)
  async addUserToEvent(@Param() {eventId}: EventIdDto, @Body() body: AddUsersToEventDto) {
    return this.saveUsersToEventUseCase.execute({id: eventId, ...body});
  }

  @Get(UserRoutes.getAllEventExpenses)
  async getAllEventExpenses(@Param() {eventId}: EventIdDto) {
    return this.getEventExpensesUseCase.execute({eventId});
  }

  @Post(UserRoutes.createExpense)
  async createExpense(@Body() expense: CreatedExpenseDto, @Param() {eventId}: EventIdDto) {
    return this.saveEventExpenseUseCase.execute({...expense, eventId});
  }
}
