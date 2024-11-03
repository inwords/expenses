import {UseCase} from '#packages/use-case';
import {Inject} from '@nestjs/common';
import {UpsertCurrencyRate} from '#persistence/currency-rate/queries/upsert-currency-rate';
import {IUpsertCurrencyRate} from '#persistence/currency-rate/types';
import {getCurrentDateWithoutTime} from '#packages/date-utils';

type Input = Record<string, number>;
export class SaveCurrencyRate implements UseCase<Input, void> {
  constructor(
    @Inject(UpsertCurrencyRate)
    private readonly upsertCurrencyRate: IUpsertCurrencyRate,
  ) {}

  public execute(rate: Input) {
    void this.upsertCurrencyRate.execute({date: getCurrentDateWithoutTime(), rate});
  }
}
