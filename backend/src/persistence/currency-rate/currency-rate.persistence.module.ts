import {Module} from '@nestjs/common';
import {FindCurrencyRate} from './queries/find-currency-rate';
import {UpsertCurrencyRate} from './queries/upsert-currency-rate';

const queries = [FindCurrencyRate, UpsertCurrencyRate];

@Module({
  providers: [...queries],
  exports: [...queries],
})
export class CurrencyRatePersistenceModule {}
