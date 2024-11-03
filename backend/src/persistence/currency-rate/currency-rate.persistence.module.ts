import {Module} from '@nestjs/common';
import {FindCurrencyRate} from './queries/find-currency-rate';
import {UpsertCurrencyRate} from './queries/upsert-currency-rate';
import {GetCurrencyRate} from '#persistence/currency-rate/queries/get-currency-rate';
import {HttpModule} from '@nestjs/axios';
import {ConfigModule} from "@nestjs/config";

const queries = [FindCurrencyRate, UpsertCurrencyRate, GetCurrencyRate];

@Module({
  imports: [HttpModule, ConfigModule],
  providers: [...queries],
  exports: [...queries],
})
export class CurrencyRatePersistenceModule {}
