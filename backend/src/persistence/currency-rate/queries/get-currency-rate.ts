import {HttpService} from '@nestjs/axios';
import {ConfigService} from '@nestjs/config';
import {IGetCurrencyRate} from '#persistence/currency-rate/types';
import {Injectable} from '@nestjs/common';
import {DateWithoutTime} from '#packages/date-utils';
import retry from 'async-retry';

@Injectable()
export class GetCurrencyRate implements IGetCurrencyRate {
  constructor(
    private readonly httpService: HttpService,
    private readonly configService: ConfigService,
  ) {}

  public async execute(date: DateWithoutTime) {
    const result = await retry(
      async () =>
        this.httpService.axiosRef.get(
          `https://openexchangerates.org/api/historical/${date}.json?app_id=${this.configService.get('OPEN_EXCHANGE_RATES_API_ID')}&base=USD`,
        ),
      {retries: 3},
    );

    const rate = result.data.rates;

    return rate || null;
  }
}
