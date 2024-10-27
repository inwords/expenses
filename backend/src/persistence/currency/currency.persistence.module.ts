import {Module} from '@nestjs/common';
import {FindCurrency} from "./queries/find-currency";

const queries = [FindCurrency];

@Module({
  providers: [...queries],
  exports: [...queries],
})
export class CurrencyPersistenceModule {}
