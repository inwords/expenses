import {Injectable, OnModuleInit} from '@nestjs/common';
import {HttpService} from '@nestjs/axios';
import {ConfigService} from '@nestjs/config';
import {SaveCurrencyRate} from '#interaction/currency-rate/use-cases/save-currency-rate';
import {IsCurrencyRateExist} from '#interaction/currency-rate/use-cases/is-currency-rate-exist';
import {getCurrentDateWithoutTime} from '#packages/date-utils';

@Injectable()
export class CurrencyRateService implements OnModuleInit {
  constructor(
    private readonly httpService: HttpService,
    private readonly configService: ConfigService,
    private readonly saveCurrencyRateUseCase: SaveCurrencyRate,
    private readonly isCurrencyRateExistUseCase: IsCurrencyRateExist,
  ) {}
  public async onModuleInit() {
    // TODO при первом запросе в течении дня идти в апишку обменника
    // TODO обработать ошибку от обменника, возмоно проставлять флаг, для необходимости пересчета
    await this.checkOrGetTodayCurrencyRate();
  }

  public async checkOrGetTodayCurrencyRate() {
    const isCurrencyForTodayExist = await this.isCurrencyRateExistUseCase.execute({date: getCurrentDateWithoutTime()});

    if (!isCurrencyForTodayExist) {
      await this.getCurrenciesChangeRates();
    }
  }

  async getCurrenciesChangeRates() {
    const result = await this.httpService.axiosRef.get(
      `https://openexchangerates.org/api/latest.json?app_id=${this.configService.get('OPEN_EXCHANGE_RATES_API_ID')}&base=USD`,
    );

    const rate = result.data.rates;

    if (rate) {
      await this.saveCurrencyRateUseCase.execute(rate);
    }
  }
}
