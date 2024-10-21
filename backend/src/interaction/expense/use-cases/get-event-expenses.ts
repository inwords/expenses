import {Inject, Injectable} from '@nestjs/common';
import {FindExpensesInput, IFindExpenses} from '../../../persistence/expense/types';
import {FindExpenses} from '../../../persistence/expense/queries/find-expenses';
import {UseCase} from '../../../packages/use-case';
import {Expense} from '../../../domain/expense/types';

type Input = FindExpensesInput;
type Output = Array<Expense>;

@Injectable()
export class GetEventExpenses implements UseCase<Input, Output> {
  constructor(
    @Inject(FindExpenses)
    private readonly findExpenses: IFindExpenses,
  ) {}

  public execute(input: Input) {
    return this.findExpenses.execute({
      where: {
        eventId: input.eventId,
      },
    });
  }
}
