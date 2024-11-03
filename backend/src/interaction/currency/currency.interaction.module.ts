import {Module} from '@nestjs/common';
import {GetAllCurrencies} from './use-cases/get-all-currencies';
import {CurrencyPersistenceModule} from '../../persistence/currency/currency.persistence.module';

const useCases = [GetAllCurrencies];

@Module({
  imports: [CurrencyPersistenceModule],
  providers: [...useCases],
  exports: [...useCases],
})
export class CurrencyInteractionModule {}
