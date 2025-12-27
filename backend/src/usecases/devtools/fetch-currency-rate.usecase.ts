import {Injectable} from '@nestjs/common';
import {UseCase} from '#packages/use-case';
import {FetchAndSaveCurrencyRateSharedUseCase} from '#usecases/shared/fetch-and-save-currency-rate.usecase';
import {ICurrencyRate} from '#domain/entities/currency-rate.entity';

type Input = {
  date: string;
};
type Output = ICurrencyRate;

@Injectable()
export class FetchCurrencyRateUseCase implements UseCase<Input, Output> {
  constructor(private readonly fetchAndSaveCurrencyRateSharedUseCase: FetchAndSaveCurrencyRateSharedUseCase) {}

  public async execute({date}: Input): Promise<Output> {
    return this.fetchAndSaveCurrencyRateSharedUseCase.execute({date});
  }
}
