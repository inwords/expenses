import {Injectable} from '@nestjs/common';
import {UseCase} from '#packages/use-case';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {EventServiceAbstract} from '#domain/abstracts/event-service/event-service';
import {IExpense} from '#domain/entities/expense.entity';
import {Result, success, isError} from '#packages/result';
import {EventNotFoundError, EventDeletedError, InvalidPinCodeError} from '#domain/errors/errors';

type Input = {eventId: string; pinCode: string};
type Output = Result<Array<IExpense>, EventNotFoundError | EventDeletedError | InvalidPinCodeError>;

@Injectable()
export class GetEventExpensesV2UseCase implements UseCase<Input, Output> {
  constructor(
    private readonly rDataService: RelationalDataServiceAbstract,
    private readonly eventService: EventServiceAbstract,
  ) {}

  public async execute({eventId, pinCode}: Input): Promise<Output> {
    const [event] = await this.rDataService.event.findById(eventId);

    const validationResult = this.eventService.isValidEvent(event, pinCode);
    if (isError(validationResult)) {
      return validationResult;
    }

    const [expenses] = await this.rDataService.expense.findByEventId(eventId);

    return success(expenses);
  }
}
