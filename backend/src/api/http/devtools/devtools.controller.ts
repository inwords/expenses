import {Controller, Get, Post, HttpCode, HttpStatus, Query, UseGuards} from '@nestjs/common';
import {ApiTags, ApiSecurity, ApiResponse, ApiOkResponse, getSchemaPath, ApiExtraModels} from '@nestjs/swagger';
import {DevtoolsRoutes} from './devtools.constants';
import {GetCurrencyRateQueryDto} from './dto/get-currency-rate.dto';
import {CurrencyRateResponseDto} from './dto/currency-rate-response.dto';
import {GetCurrencyRateUseCase} from '#usecases/devtools/get-currency-rate.usecase';
import {FetchCurrencyRateUseCase} from '#usecases/devtools/fetch-currency-rate.usecase';
import {DevtoolsSecretGuard} from './guards/devtools-secret.guard';

@Controller(DevtoolsRoutes.root)
@ApiTags('Devtools')
@UseGuards(DevtoolsSecretGuard)
@ApiSecurity('devtools-secret')
@ApiExtraModels(CurrencyRateResponseDto)
export class DevtoolsController {
  constructor(
    private readonly getCurrencyRateUseCase: GetCurrencyRateUseCase,
    private readonly fetchCurrencyRateUseCase: FetchCurrencyRateUseCase,
  ) {}

  @Get(DevtoolsRoutes.getCurrencyRate)
  @HttpCode(HttpStatus.OK)
  @ApiOkResponse({
    schema: {
      oneOf: [{$ref: getSchemaPath(CurrencyRateResponseDto)}, {type: 'null'}],
    },
  })
  async getCurrencyRate(@Query() query: GetCurrencyRateQueryDto): Promise<CurrencyRateResponseDto | null> {
    return this.getCurrencyRateUseCase.execute({date: query.date});
  }

  @Post(DevtoolsRoutes.fetchCurrencyRate)
  @HttpCode(HttpStatus.OK)
  @ApiResponse({status: HttpStatus.OK, type: CurrencyRateResponseDto})
  async fetchCurrencyRate(@Query() query: GetCurrencyRateQueryDto): Promise<CurrencyRateResponseDto> {
    return this.fetchCurrencyRateUseCase.execute({date: query.date});
  }
}
