import {Body, Controller, Delete, Get, HttpCode, Param, Post, Query} from '@nestjs/common';
import {UserRoutes, UserV2Routes} from './user.contants';
import {ApiTags} from '@nestjs/swagger';

import {CrateEventBodyDto} from './dto/create-event.dto';
import {GetEventInfoQueryDto} from './dto/get-event-info.dto';
import {EventIdDto} from './dto/event-id.dto';
import {AddUsersToEventDto} from './dto/add-users-to-event.dto';
import {CreatedExpenseDto} from './dto/create-expense.dto';
import {DeleteEventBodyDto} from './dto/delete-event.dto';
import {GetEventInfoBodyDto} from './dto/get-event-info-body.dto';
import {GetEventExpensesBodyDto} from './dto/get-event-expenses-body.dto';
import {CreateExpenseV2Dto} from './dto/create-expense-v2.dto';

import {GetAllCurrenciesUseCase} from '#usecases/users/get-all-currencies.usecase';
import {GetEventExpensesUseCase} from '#usecases/users/get-event-expenses.usecase';
import {SaveEventExpenseUseCase} from '#usecases/users/save-event-expense.usecase';
import {SaveEventUseCase} from '#usecases/users/save-event.usecase';
import {GetEventInfoUseCase} from '#usecases/users/get-event-info.usecase';
import {SaveUsersToEventUseCase} from '#usecases/users/save-users-to-event.usecase';
import {DeleteEventUseCase} from '#usecases/users/delete-event.usecase';
import {
  GetEventInfoV2UseCase,
  SaveUsersToEventV2UseCase,
  SaveEventExpenseV2UseCase,
  GetEventExpensesV2UseCase,
} from '#usecases/users/v2';

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
    // V2 use cases
    private readonly getEventInfoV2UseCase: GetEventInfoV2UseCase,
    private readonly saveUsersToEventV2UseCase: SaveUsersToEventV2UseCase,
    private readonly saveEventExpenseV2UseCase: SaveEventExpenseV2UseCase,
    private readonly getEventExpensesV2UseCase: GetEventExpensesV2UseCase,
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

  @Delete(UserRoutes.deleteEvent)
  @HttpCode(200)
  async deleteEvent(@Param() {eventId}: EventIdDto, @Body() body: DeleteEventBodyDto) {
    return this.deleteEventUseCase.execute({eventId, pinCode: body.pinCode});
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

  // V2 ENDPOINTS - POST methods with PIN in body for better security
  @Post(`${UserV2Routes.root}${UserV2Routes.getEventInfo}`)
  @ApiTags('User V2')
  async getEventInfoV2(@Param() {eventId}: EventIdDto, @Body() body: GetEventInfoBodyDto) {
    return this.getEventInfoV2UseCase.execute({eventId, pinCode: body.pinCode});
  }

  @Post(`${UserV2Routes.root}${UserV2Routes.addUsersToEvent}`)
  @HttpCode(201)
  @ApiTags('User V2')
  async addUserToEventV2(@Param() {eventId}: EventIdDto, @Body() body: AddUsersToEventDto) {
    return this.saveUsersToEventV2UseCase.execute({eventId, ...body});
  }

  @Post(`${UserV2Routes.root}${UserV2Routes.getAllEventExpenses}`)
  @ApiTags('User V2')
  async getAllEventExpensesV2(@Param() {eventId}: EventIdDto, @Body() body: GetEventExpensesBodyDto) {
    return this.getEventExpensesV2UseCase.execute({eventId, pinCode: body.pinCode});
  }

  @Post(`${UserV2Routes.root}${UserV2Routes.createExpense}`)
  @ApiTags('User V2')
  async createExpenseV2(@Body() expense: CreateExpenseV2Dto, @Param() {eventId}: EventIdDto) {
    return this.saveEventExpenseV2UseCase.execute({...expense, eventId});
  }
}
