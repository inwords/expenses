import {Body, Controller} from '@nestjs/common';
import {GrpcMethod} from '@nestjs/microservices';
import {CrateEventBodyDto} from '#api/http/user/dto/create-event.dto';
import {EventIdDto} from '#api/http/user/dto/event-id.dto';
import {GetEventInfoQueryDto} from '#api/http/user/dto/get-event-info.dto';
import {AddUsersToEventDto} from '#api/http/user/dto/add-users-to-event.dto';
import {CreatedExpenseDto} from '#api/http/user/dto/create-expense.dto';
import {DeleteEventBodyDto} from '#api/http/user/dto/delete-event.dto';
import {GetEventExpensesBodyDto} from '#api/http/user/dto/get-event-expenses-body.dto';
import {CreateExpenseV2Dto} from '#api/http/user/dto/create-expense-v2.dto';
import {GetEventExpensesUseCase} from '#usecases/users/get-event-expenses.usecase';
import {SaveEventExpenseUseCase} from '#usecases/users/save-event-expense.usecase';
import {GetAllCurrenciesUseCase} from '#usecases/users/get-all-currencies.usecase';
import {SaveEventUseCase} from '#usecases/users/save-event.usecase';
import {GetEventInfoUseCase} from '#usecases/users/get-event-info.usecase';
import {SaveUsersToEventUseCase} from '#usecases/users/save-users-to-event.usecase';
import {DeleteEventUseCase} from '#usecases/users/delete-event.usecase';
import {
  GetEventInfoV2UseCase,
  SaveUsersToEventV2UseCase,
  SaveEventExpenseV2UseCase,
  GetEventExpensesV2UseCase,
  CreateEventShareTokenV2UseCase,
} from '#usecases/users/v2';
import {isError} from '#packages/result';

@Controller()
export class UserController {
  constructor(
    private readonly getEventExpensesUseCase: GetEventExpensesUseCase,
    private readonly saveEventExpenseUseCase: SaveEventExpenseUseCase,
    private readonly getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
    private readonly saveEventUseCase: SaveEventUseCase,
    private readonly getEventInfoUseCase: GetEventInfoUseCase,
    private readonly saveUsersToEventUseCase: SaveUsersToEventUseCase,
    private readonly deleteEventUseCase: DeleteEventUseCase,
    private readonly getEventInfoV2UseCase: GetEventInfoV2UseCase,
    private readonly saveUsersToEventV2UseCase: SaveUsersToEventV2UseCase,
    private readonly saveEventExpenseV2UseCase: SaveEventExpenseV2UseCase,
    private readonly getEventExpensesV2UseCase: GetEventExpensesV2UseCase,
    private readonly createEventShareTokenV2UseCase: CreateEventShareTokenV2UseCase,
  ) {}

  @GrpcMethod('UserService', 'GetAllCurrencies')
  async getAllCurrencies() {
    const result = await this.getAllCurrenciesUseCase.execute();

    if (isError(result)) {
      throw result.error;
    }

    return {currencies: result.value};
  }

  @GrpcMethod('UserService', 'CreateEvent')
  async createEvent(@Body() body: CrateEventBodyDto) {
    const {users, ...event} = body;

    const result = await this.saveEventUseCase.execute({users, event});

    if (isError(result)) {
      throw result.error;
    }

    return result.value;
  }

  @GrpcMethod('UserService', 'GetEventInfo')
  async getEventInfo(@Body() {eventId, pinCode}: EventIdDto & GetEventInfoQueryDto) {
    const result = await this.getEventInfoUseCase.execute({eventId, pinCode});

    if (isError(result)) {
      throw result.error;
    }

    return result.value;
  }

  @GrpcMethod('UserService', 'DeleteEvent')
  async deleteEvent(@Body() body: EventIdDto & DeleteEventBodyDto) {
    const {eventId, pinCode} = body;
    const result = await this.deleteEventUseCase.execute({eventId, pinCode});

    if (isError(result)) {
      throw result.error;
    }

    return result.value;
  }

  @GrpcMethod('UserService', 'AddUsersToEvent')
  async addUserToEvent(@Body() body: AddUsersToEventDto & EventIdDto) {
    const {eventId, ...rest} = body;

    const result = await this.saveUsersToEventUseCase.execute({eventId, ...rest});

    if (isError(result)) {
      throw result.error;
    }

    return {users: result.value};
  }

  @GrpcMethod('UserService', 'GetAllEventExpenses')
  async getAllEventExpenses(@Body() {eventId}: EventIdDto) {
    const result = await this.getEventExpensesUseCase.execute({eventId});

    if (isError(result)) {
      throw result.error;
    }

    return {expenses: result.value};
  }

  @GrpcMethod('UserService', 'CreateExpense')
  async createExpense(@Body() expense: CreatedExpenseDto & EventIdDto) {
    const result = await this.saveEventExpenseUseCase.execute(expense);

    if (isError(result)) {
      throw result.error;
    }

    return result.value;
  }

  @GrpcMethod('UserService', 'GetEventInfoV2')
  async getEventInfoV2(@Body() {eventId, pinCode, token}: EventIdDto & GetEventInfoQueryDto & {token?: string}) {
    const result = await this.getEventInfoV2UseCase.execute({eventId, pinCode, token});

    if (isError(result)) {
      throw result.error;
    }

    return result.value;
  }

  @GrpcMethod('UserService', 'AddUsersToEventV2')
  async addUserToEventV2(@Body() body: AddUsersToEventDto & EventIdDto) {
    const {eventId, ...rest} = body;

    const result = await this.saveUsersToEventV2UseCase.execute({eventId, ...rest});

    if (isError(result)) {
      throw result.error;
    }

    return {users: result.value};
  }

  @GrpcMethod('UserService', 'GetAllEventExpensesV2')
  async getAllEventExpensesV2(@Body() {eventId, pinCode}: EventIdDto & GetEventExpensesBodyDto) {
    const result = await this.getEventExpensesV2UseCase.execute({eventId, pinCode});

    if (isError(result)) {
      throw result.error;
    }

    return {expenses: result.value};
  }

  @GrpcMethod('UserService', 'CreateExpenseV2')
  async createExpenseV2(@Body() expense: CreateExpenseV2Dto & EventIdDto) {
    const result = await this.saveEventExpenseV2UseCase.execute(expense);

    if (isError(result)) {
      throw result.error;
    }

    return result.value;
  }

  @GrpcMethod('UserService', 'CreateEventShareTokenV2')
  async createEventShareTokenV2(@Body() {eventId, pinCode}: EventIdDto & {pinCode: string}) {
    const result = await this.createEventShareTokenV2UseCase.execute({eventId, pinCode});

    if (isError(result)) {
      throw result.error;
    }

    return result.value;
  }
}
