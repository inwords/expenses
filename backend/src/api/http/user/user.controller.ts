import {Body, Controller, Get, HttpCode, Param, Post, Query} from '@nestjs/common';
import {UserRoutes} from './user.contants';
import {ApiTags} from '@nestjs/swagger';

import {CrateEventBodyDto} from './dto/create-event.dto';
import {GetEventInfoQueryDto} from './dto/get-event-info.dto';
import {EventIdDto} from './dto/event-id.dto';
import {AddUsersToEventDto} from './dto/add-users-to-event.dto';
import {CreatedExpenseDto} from './dto/create-expense.dto';
import {CurrencyService} from '../../../currency/currency.service';
import {EventService} from '../../../event/event.service';
import {GetEventExpenses} from '../../../interaction/expense/use-cases/get-event-expenses';
import {SaveEventExpense} from '../../../interaction/expense/use-cases/save-event-expense';

@Controller(UserRoutes.root)
@ApiTags('User')
export class UserController {
  constructor(
    private readonly currencyService: CurrencyService,
    private readonly eventService: EventService,
    private readonly getEventExpensesUseCase: GetEventExpenses,
    private readonly saveEventExpenseUseCase: SaveEventExpense,
  ) {}
  @Get(UserRoutes.getAllCurrencies)
  @HttpCode(200)
  async getAllCurrencies() {
    return this.currencyService.getAllCurrencies();
  }

  @Post(UserRoutes.createEvent)
  @HttpCode(201)
  async createEvent(@Body() body: CrateEventBodyDto) {
    return this.eventService.saveEvent(body);
  }

  @Get(UserRoutes.getEventInfo)
  async getEventInfo(@Param() {eventId}: EventIdDto, @Query() query: GetEventInfoQueryDto) {
    return this.eventService.getEventInfo(eventId, query.pinCode);
  }

  @Post(UserRoutes.addUsersToEvent)
  @HttpCode(201)
  async addUserToEvent(@Param() {eventId}: EventIdDto, @Body() body: AddUsersToEventDto) {
    return this.eventService.addUsersToEvent(eventId, body.users, body.pinCode);
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
