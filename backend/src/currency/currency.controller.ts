import {Controller, Get, HttpCode} from '@nestjs/common';
import {CURRENCY_ROUTES} from './constants';
import {CurrencyService} from './currency.service';

@Controller(CURRENCY_ROUTES.root)
export class CurrencyController {
  constructor(private readonly currencyService: CurrencyService) {}

  @Get(CURRENCY_ROUTES.getAllCurrencies)
  @HttpCode(200)
  async getAllCurrencies() {
    return this.currencyService.getAllCurrencies();
  }
}
