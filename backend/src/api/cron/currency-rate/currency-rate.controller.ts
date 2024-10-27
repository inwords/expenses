import {Cron, CronExpression} from '@nestjs/schedule';
import {Controller} from '@nestjs/common';
import {CurrencyRateService} from "./currency-rate.service";

@Controller()
export class CurrencyRateController {
  constructor(
    private readonly currencyRateService: CurrencyRateService,
  ) {}
  @Cron(CronExpression.EVERY_DAY_AT_MIDNIGHT)
  async getCurrenciesChangeRates() {
   await this.currencyRateService.getCurrenciesChangeRates();
  }
}
