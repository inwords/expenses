import {Injectable} from '@nestjs/common';
import {UseCase} from '#packages/use-case';
import {FetchAndSaveCurrencyRateSharedUseCase} from '#usecases/shared/fetch-and-save-currency-rate.usecase';
import {getCurrentDateWithoutTimeUTC} from '#packages/date-utils';

type Input = void;
type Output = void;

@Injectable()
export class FetchDailyCurrencyRatesUseCase implements UseCase<Input, Output> {
  constructor(private readonly fetchAndSaveCurrencyRateSharedUseCase: FetchAndSaveCurrencyRateSharedUseCase) {}

  public async execute(): Promise<void> {
    const date = getCurrentDateWithoutTimeUTC();

    await this.fetchAndSaveCurrencyRateSharedUseCase.execute({date});
  }
}
