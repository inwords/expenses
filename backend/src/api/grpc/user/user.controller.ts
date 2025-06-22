import {Body, Controller} from '@nestjs/common';
import {GrpcMethod} from '@nestjs/microservices';
import {CrateEventBodyDto} from '#api/http/user/dto/create-event.dto';
import {EventIdDto} from '#api/http/user/dto/event-id.dto';
import {GetEventInfoQueryDto} from '#api/http/user/dto/get-event-info.dto';
import {AddUsersToEventDto} from '#api/http/user/dto/add-users-to-event.dto';
import {CreatedExpenseDto} from '#api/http/user/dto/create-expense.dto';
import {GetEventExpensesUseCase} from '#usecases/users/get-event-expenses.usecase';
import {SaveEventExpenseUseCase} from '#usecases/users/save-event-expense.usecase';
import {GetAllCurrenciesUseCase} from '#usecases/users/get-all-currencies.usecase';
import {SaveEventUseCase} from '#usecases/users/save-event.usecase';
import {GetEventInfoUseCase} from '#usecases/users/get-event-info.usecase';
import {SaveUsersToEventUseCase} from '#usecases/users/save-users-to-event.usecase';

@Controller()
export class UserController {
  constructor(
    private readonly getEventExpensesUseCase: GetEventExpensesUseCase,
    private readonly saveEventExpenseUseCase: SaveEventExpenseUseCase,
    private readonly getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
    private readonly saveEventUseCase: SaveEventUseCase,
    private readonly getEventInfoUseCase: GetEventInfoUseCase,
    private readonly saveUsersToEventUseCase: SaveUsersToEventUseCase,
  ) {}

  @GrpcMethod('UserService', 'GetAllCurrencies')
  async getAllCurrencies() {
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

    return {users: await this.saveUsersToEventUseCase.execute({eventId, ...rest})};
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
