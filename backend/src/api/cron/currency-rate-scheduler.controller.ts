import {Injectable} from '@nestjs/common';
import {Cron, CronExpression} from '@nestjs/schedule';
import {FetchDailyCurrencyRatesUseCase} from '#usecases/cron/fetch-daily-currency-rates.usecase';

@Injectable()
export class CurrencyRateSchedulerController {
  constructor(private readonly fetchDailyCurrencyRatesUseCase: FetchDailyCurrencyRatesUseCase) {}

  @Cron('0 0 * * *', {
    timeZone: 'UTC'
  })
  async handleCron() {
    await this.fetchDailyCurrencyRatesUseCase.execute();
  }
}
