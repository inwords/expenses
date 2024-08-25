import {Module} from '@nestjs/common';
import {ScheduleModule} from '@nestjs/schedule';
import {HttpModule} from '@nestjs/axios';
import {ConfigModule} from '@nestjs/config';
import {CurrencyRateService} from './currency-rate.service';
import {CurrencyRateController} from './currency-rate.controller';

@Module({
  imports: [ScheduleModule.forRoot(), HttpModule, ConfigModule],
  controllers: [CurrencyRateController],
  providers: [CurrencyRateService],
  exports: [CurrencyRateService],
})
export class CurrencyRateModule {}
