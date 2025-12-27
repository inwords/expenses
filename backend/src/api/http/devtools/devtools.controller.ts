import {Controller, Get, Post, HttpCode, Query, UseGuards} from '@nestjs/common';
import {ApiTags, ApiSecurity} from '@nestjs/swagger';
import {DevtoolsRoutes} from './devtools.constants';
import {GetCurrencyRateQueryDto} from './dto/get-currency-rate.dto';
import {GetCurrencyRateUseCase} from '#usecases/devtools/get-currency-rate.usecase';
import {FetchCurrencyRateUseCase} from '#usecases/devtools/fetch-currency-rate.usecase';
import {DevtoolsSecretGuard} from './guards/devtools-secret.guard';

@Controller(DevtoolsRoutes.root)
@ApiTags('Devtools')
@UseGuards(DevtoolsSecretGuard)
@ApiSecurity('devtools-secret')
export class DevtoolsController {
  constructor(
    private readonly getCurrencyRateUseCase: GetCurrencyRateUseCase,
    private readonly fetchCurrencyRateUseCase: FetchCurrencyRateUseCase,
  ) {}

  @Get(DevtoolsRoutes.getCurrencyRate)
  @HttpCode(200)
  async getCurrencyRate(@Query() query: GetCurrencyRateQueryDto) {
    return this.getCurrencyRateUseCase.execute({date: query.date});
  }

  @Post(DevtoolsRoutes.fetchCurrencyRate)
  @HttpCode(200)
  async fetchCurrencyRate(@Query() query: GetCurrencyRateQueryDto) {
    return this.fetchCurrencyRateUseCase.execute({date: query.date});
  }
}
