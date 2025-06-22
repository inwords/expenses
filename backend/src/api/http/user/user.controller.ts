import {Body, Controller, Get, HttpCode, Param, Post, Query} from '@nestjs/common';
import {UserRoutes} from './user.contants';
import {ApiTags} from '@nestjs/swagger';

import {CrateEventBodyDto} from './dto/create-event.dto';
import {GetEventInfoQueryDto} from './dto/get-event-info.dto';
import {EventIdDto} from './dto/event-id.dto';
import {AddUsersToEventDto} from './dto/add-users-to-event.dto';
import {CreatedExpenseDto} from './dto/create-expense.dto';

import {GetAllCurrenciesUseCase} from '#usecases/users/get-all-currencies.usecase';
import {GetEventExpensesUseCase} from '#usecases/users/get-event-expenses.usecase';
import {SaveEventExpenseUseCase} from '#usecases/users/save-event-expense.usecase';
import {SaveEventUseCase} from '#usecases/users/save-event.usecase';
import {GetEventInfoUseCase} from '#usecases/users/get-event-info.usecase';
import {SaveUsersToEventUseCase} from '#usecases/users/save-users-to-event.usecase';

@Controller(UserRoutes.root)
@ApiTags('User')
export class UserController {
  constructor(
    private readonly getEventExpensesUseCase: GetEventExpensesUseCase,
    private readonly saveEventExpenseUseCase: SaveEventExpenseUseCase,
    private readonly getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
    private readonly saveEventUseCase: SaveEventUseCase,
    private readonly getEventInfoUseCase: GetEventInfoUseCase,
    private readonly saveUsersToEventUseCase: SaveUsersToEventUseCase,
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
    return this.saveUsersToEventUseCase.execute({eventId, ...body});
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
