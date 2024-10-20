import {Inject, Injectable} from '@nestjs/common';
import {UseCase} from '../../../packages/use-case';
import {IUpsertExpense, UpsertExpenseInput} from '../../../persistence/expense/types';
import {Expense} from '../../../domain/expense/types';
import {UpsertExpense} from '../../../persistence/expense/queries/upsert-expense';

type Input = Omit<UpsertExpenseInput, 'createdAt'>;
type Output = Expense;

@Injectable()
export class SaveEventExpense implements UseCase<Input, Output> {
  constructor(
    @Inject(UpsertExpense)
    private readonly upsertExpense: IUpsertExpense,
  ) {}

  public async execute(input: Input) {
    //TODO описать логику конвертации валюты
    return this.upsertExpense.execute({...input, createdAt: new Date().toISOString()});
  }
}
