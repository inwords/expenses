import {Module} from '@nestjs/common';
import {ExpenseController} from './expense.controller';
import {ExpenseService} from './expense.service';
import {CurrencyRateModule} from '../currency-rate/currency-rate.module';

@Module({
  imports: [CurrencyRateModule],
  controllers: [ExpenseController],
  providers: [ExpenseService],
})
export class ExpenseModule {}
