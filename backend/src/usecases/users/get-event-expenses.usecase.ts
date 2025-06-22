import {Injectable} from '@nestjs/common';
import {UseCase} from '#packages/use-case';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {IExpense} from '#domain/entities/expense.entity';

type Input = Pick<IExpense, 'eventId'>;
type Output = Array<IExpense>;

@Injectable()
export class GetEventExpensesUseCase implements UseCase<Input, Output> {
  constructor(private readonly rDataService: RelationalDataServiceAbstract) {}

  public async execute({eventId}: Input) {
    const [expenses] = await this.rDataService.expense.findByEventId(eventId);

    return expenses;
  }
}
