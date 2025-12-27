import {Module} from '@nestjs/common';
import {CurrencyRateSchedulerController} from './currency-rate-scheduler.controller';
import {UseCasesModule} from '#usecases/usecases.layer';

@Module({
  imports: [UseCasesModule],
  providers: [CurrencyRateSchedulerController],
})
export class CronModule {}
