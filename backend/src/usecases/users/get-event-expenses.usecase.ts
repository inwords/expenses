import {Injectable} from '@nestjs/common';
import {UseCase} from '#packages/use-case';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {EventServiceAbstract} from '#domain/abstracts/event-service/event-service';
import {IExpense} from '#domain/entities/expense.entity';
import {Result, success, isError} from '#packages/result';
import {EventNotFoundError, EventDeletedError} from '#domain/errors/errors';

type Input = Pick<IExpense, 'eventId'>;
type Output = Result<Array<IExpense>, EventNotFoundError | EventDeletedError>;

@Injectable()
export class GetEventExpensesUseCase implements UseCase<Input, Output> {
  constructor(
    private readonly rDataService: RelationalDataServiceAbstract,
    private readonly eventService: EventServiceAbstract,
  ) {}

  public async execute({eventId}: Input): Promise<Output> {
    const [event] = await this.rDataService.event.findById(eventId);

    const eventExistsResult = this.eventService.isEventExists(event);
    if (isError(eventExistsResult)) {
      return eventExistsResult;
    }

    const eventNotDeletedResult = this.eventService.isEventNotDeleted(event);
    if (isError(eventNotDeletedResult)) {
      return eventNotDeletedResult;
    }

    const [expenses] = await this.rDataService.expense.findByEventId(eventId);

    return success(expenses);
  }
}
