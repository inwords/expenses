import {Body, Controller, Delete, Get, HttpCode, Param, Post, Query} from '@nestjs/common';
import {UserRoutes} from './user.constants';
import {ApiTags} from '@nestjs/swagger';

import {CrateEventBodyDto} from './dto/create-event.dto';
import {GetEventInfoQueryDto} from './dto/get-event-info.dto';
import {EventIdDto} from './dto/event-id.dto';
import {AddUsersToEventDto} from './dto/add-users-to-event.dto';
import {CreatedExpenseDto} from './dto/create-expense.dto';
import {DeleteEventBodyDto} from './dto/delete-event.dto';

import {GetAllCurrenciesUseCase} from '#usecases/users/get-all-currencies.usecase';
import {GetEventExpensesUseCase} from '#usecases/users/get-event-expenses.usecase';
import {SaveEventExpenseUseCase} from '#usecases/users/save-event-expense.usecase';
import {SaveEventUseCase} from '#usecases/users/save-event.usecase';
import {GetEventInfoUseCase} from '#usecases/users/get-event-info.usecase';
import {SaveUsersToEventUseCase} from '#usecases/users/save-users-to-event.usecase';
import {DeleteEventUseCase} from '#usecases/users/delete-event.usecase';
import {isError} from '#packages/result';

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
    private readonly deleteEventUseCase: DeleteEventUseCase,
  ) {}

  @Get(UserRoutes.getAllCurrencies)
  @HttpCode(200)
  async getAllCurrencies() {
    const result = await this.getAllCurrenciesUseCase.execute();

    if (isError(result)) {
      throw result.error;
    }

    return result.value;
  }

  @Post(UserRoutes.createEvent)
  @HttpCode(201)
  async createEvent(@Body() body: CrateEventBodyDto) {
    const {users, ...event} = body;

    const result = await this.saveEventUseCase.execute({users, event});

    if (isError(result)) {
      throw result.error;
    }

    return result.value;
  }

  @Get(UserRoutes.getEventInfo)
  async getEventInfo(@Param() {eventId}: EventIdDto, @Query() query: GetEventInfoQueryDto) {
    const result = await this.getEventInfoUseCase.execute({eventId, pinCode: query.pinCode});

    if (isError(result)) {
      throw result.error;
    }

    return result.value;
  }

  @Delete(UserRoutes.deleteEvent)
  @HttpCode(200)
  async deleteEvent(@Param() {eventId}: EventIdDto, @Body() body: DeleteEventBodyDto) {
    const result = await this.deleteEventUseCase.execute({eventId, pinCode: body.pinCode});

    if (isError(result)) {
      throw result.error;
    }

    return result.value;
  }

  @Post(UserRoutes.addUsersToEvent)
  @HttpCode(201)
  async addUserToEvent(@Param() {eventId}: EventIdDto, @Body() body: AddUsersToEventDto) {
    const result = await this.saveUsersToEventUseCase.execute({eventId, ...body});

    if (isError(result)) {
      throw result.error;
    }

    return result.value;
  }

  @Get(UserRoutes.getAllEventExpenses)
  async getAllEventExpenses(@Param() {eventId}: EventIdDto) {
    const result = await this.getEventExpensesUseCase.execute({eventId});

    if (isError(result)) {
      throw result.error;
    }

    return result.value;
  }

  @Post(UserRoutes.createExpense)
  async createExpense(@Body() expense: CreatedExpenseDto, @Param() {eventId}: EventIdDto) {
    const result = await this.saveEventExpenseUseCase.execute({...expense, eventId});

    if (isError(result)) {
      throw result.error;
    }

    return result.value;
  }
}
