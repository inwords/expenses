import {Body, Controller} from '@nestjs/common';
import {GrpcMethod} from '@nestjs/microservices';
import {GetEventExpenses} from '#interaction/expense/use-cases/get-event-expenses';
import {SaveEventExpense} from '#interaction/expense/use-cases/save-event-expense';
import {GetAllCurrencies} from '#interaction/currency/use-cases/get-all-currencies';
import {SaveEvent} from '#interaction/event/use-cases/save-event';
import {GetEventInfo} from '#interaction/event/use-cases/get-event-info';
import {SaveUsersToEvent} from '#interaction/user/use-cases/save-users-to-event';
import {CrateEventBodyDto} from '#api/http/user/dto/create-event.dto';
import {EventIdDto} from '#api/http/user/dto/event-id.dto';
import {GetEventInfoQueryDto} from '#api/http/user/dto/get-event-info.dto';
import {AddUsersToEventDto} from '#api/http/user/dto/add-users-to-event.dto';
import {CreatedExpenseDto} from '#api/http/user/dto/create-expense.dto';

@Controller()
export class UserController {
  constructor(
    private readonly getEventExpensesUseCase: GetEventExpenses,
    private readonly saveEventExpenseUseCase: SaveEventExpense,
    private readonly getAllCurrenciesUseCase: GetAllCurrencies,
    private readonly saveEventUseCase: SaveEvent,
    private readonly getEventInfoUseCase: GetEventInfo,
    private readonly saveUsersToEventUseCase: SaveUsersToEvent,
  ) {}

  @GrpcMethod('UserService', 'GetAllCurrencies')
  async getAllCurrencies() {
    console.log('lek')
    return {currencies: await this.getAllCurrenciesUseCase.execute()};
  }

  @GrpcMethod('UserService', 'CreateEvent')
  async createEvent(@Body() body: CrateEventBodyDto) {
    const {users, ...event} = body;

    return this.saveEventUseCase.execute({users, event});
  }

  @GrpcMethod('UserService', 'GetEventInfo')
  async getEventInfo(@Body() {eventId, pinCode}: EventIdDto & GetEventInfoQueryDto) {
    return await this.getEventInfoUseCase.execute({eventId, pinCode});
  }

  @GrpcMethod('UserService', 'AddUsersToEvent')
  async addUserToEvent(@Body() body: AddUsersToEventDto & EventIdDto) {
    const {eventId, ...rest} = body;

    return {users: await this.saveUsersToEventUseCase.execute({id: eventId, ...rest})};
  }

  @GrpcMethod('UserService', 'GetAllEventExpenses')
  async getAllEventExpenses(@Body() {eventId}: EventIdDto) {
    const resp = {expenses: await this.getEventExpensesUseCase.execute({eventId})};

    return resp;
  }

  @GrpcMethod('UserService', 'CreateExpense')
  async createExpense(@Body() expense: CreatedExpenseDto & EventIdDto) {
    return this.saveEventExpenseUseCase.execute(expense);
  }
}
