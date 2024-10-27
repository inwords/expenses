import {Module} from '@nestjs/common';
import {CurrencyRatePersistenceModule} from '../../persistence/currency-rate/currency-rate.persistence.module';
import {SaveCurrencyRate} from './use-cases/save-currency-rate';
import {IsCurrencyRateExist} from './use-cases/is-currency-rate-exist';

const useCases = [SaveCurrencyRate, IsCurrencyRateExist];

@Module({
  imports: [CurrencyRatePersistenceModule],
  providers: [...useCases],
  exports: [...useCases],
})
export class CurrencyRateInteractionModule {}
