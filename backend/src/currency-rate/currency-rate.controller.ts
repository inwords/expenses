import {HttpService} from '@nestjs/axios';
import {ConfigService} from '@nestjs/config';
import {Cron, CronExpression} from '@nestjs/schedule';
import {CurrencyRateService} from './currency-rate.service';
import {Controller} from '@nestjs/common';

@Controller()
export class CurrencyRateController {
  constructor(
    private readonly httpService: HttpService,
    private readonly configService: ConfigService,
    private readonly currencyRateService: CurrencyRateService,
  ) {}
  @Cron(CronExpression.EVERY_DAY_AT_MIDNIGHT)
  async getCurrenciesChangeRates() {
    const result = await this.httpService.axiosRef.get(
      `https://openexchangerates.org/api/latest.json?app_id=${this.configService.get('OPEN_EXCHANGE_RATES_API_ID')}&base=EUR`,
    );

    const rates = JSON.parse(result.data).rates;

    if (rates) {
      await this.currencyRateService.setCurrenciesChangeRates(rates);
    }
  }
}
