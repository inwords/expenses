import {Inject} from '@nestjs/common';
import {FindCurrencyRate} from '../../../persistence/currency-rate/queries/find-currency-rate';
import {IFindCurrencyRate} from '../../../persistence/currency-rate/types';
import {UseCase} from '../../../packages/use-case';

type Input = {date: string};
type Output = boolean;

export class IsCurrencyRateExist implements UseCase<Input, Output> {
  constructor(
    @Inject(FindCurrencyRate)
    private readonly findCurrencyRate: IFindCurrencyRate,
  ) {}

  public async execute({date}: Input) {
    const currencyRate = await this.findCurrencyRate.execute({date});

    return Boolean(currencyRate);
  }
}
