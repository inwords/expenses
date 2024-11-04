import {HttpService} from '@nestjs/axios';
import {ConfigService} from '@nestjs/config';
import {IGetCurrencyRate} from '#persistence/currency-rate/types';
import {Injectable} from "@nestjs/common";

@Injectable()
export class GetCurrencyRate implements IGetCurrencyRate {
  constructor(
    private readonly httpService: HttpService,
    private readonly configService: ConfigService,
  ) {}

  public async execute() {
    const result = await this.httpService.axiosRef.get(
      `https://openexchangerates.org/api/latest.json?app_id=${this.configService.get('OPEN_EXCHANGE_RATES_API_ID')}&base=USD`,
    );
    console.log("-> result", result);

    const rate = result.data.rates;
    console.log("-> rate", rate);

    return rate || null;
  }
}
