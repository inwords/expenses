import {Injectable} from '@nestjs/common';
import {UseCase} from '#packages/use-case';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {CurrencyRateServiceAbstract} from '#domain/abstracts/currency-rate-service/currency-rate-service';
import {CurrencyRateValueObject} from '#domain/value-objects/currency-rate.value-object';
import {getCurrentDateWithoutTimeUTC} from '#packages/date-utils';

type Input = void;
type Output = void;

@Injectable()
export class FetchDailyCurrencyRatesUseCase implements UseCase<Input, Output> {
  constructor(
    private readonly rDataService: RelationalDataServiceAbstract,
    private readonly currencyRateService: CurrencyRateServiceAbstract,
  ) {}

  public async execute(): Promise<void> {
    const date = getCurrentDateWithoutTimeUTC();

    const rates = await this.currencyRateService.getCurrencyRate(date);

    if (!rates) {
      throw new Error(`Failed to fetch currency rates for ${date}`);
    }

    const currencyRate = new CurrencyRateValueObject({
      date,
      rate: rates,
    }).value;

    await this.rDataService.currencyRate.insert(currencyRate);
  }
}
