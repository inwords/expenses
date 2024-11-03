import {Module} from '@nestjs/common';
import {FindCurrency} from "./queries/find-currency";
import {FindCurrencies} from "./queries/find-currencies";

const queries = [FindCurrency, FindCurrencies];

@Module({
  providers: [...queries],
  exports: [...queries],
})
export class CurrencyPersistenceModule {}
