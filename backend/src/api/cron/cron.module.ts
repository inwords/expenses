import {Module} from '@nestjs/common';
import {CurrencyRateController} from './currency-rate/currency-rate.controller';
import {CurrencyRateInteractionModule} from '#interaction/currency-rate/currency-rate.interaction.module';
import {ScheduleModule} from '@nestjs/schedule';
import {ConfigModule} from '@nestjs/config';
import {HttpModule} from '@nestjs/axios';
import {CurrencyRateService} from './currency-rate/currency-rate.service';

@Module({
  imports: [CurrencyRateInteractionModule, ScheduleModule.forRoot(), HttpModule, ConfigModule],
  controllers: [CurrencyRateController],
  providers: [CurrencyRateService],
})
export class CronModule {}
