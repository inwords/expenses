import {Injectable} from '@nestjs/common';
import {UseCase} from '#packages/use-case';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {EventServiceAbstract} from '#domain/abstracts/event-service/event-service';
import {IExpense} from '#domain/entities/expense.entity';

type Input = {eventId: string; pinCode: string};
type Output = Array<IExpense>;

@Injectable()
export class GetEventExpensesV2UseCase implements UseCase<Input, Output> {
  constructor(
    private readonly rDataService: RelationalDataServiceAbstract,
    private readonly eventService: EventServiceAbstract,
  ) {}

  public async execute({eventId, pinCode}: Input): Promise<Output> {
    const [event] = await this.rDataService.event.findById(eventId);

    // ADDED: Critical security validations - prevent unauthorized access
    this.eventService.validateEvent(event, pinCode);

    const [expenses] = await this.rDataService.expense.findByEventId(eventId);

    return expenses;
  }
}
