import {Module} from '@nestjs/common';
import {ExpenseService} from './expense.service';
import {CurrencyRateModule} from '../currency-rate/currency-rate.module';
import {ExpensePersistenceModule} from "../persistence/expense/expense.persistence.module";

@Module({
  imports: [CurrencyRateModule, ExpensePersistenceModule],
  providers: [ExpenseService],
  exports: [ExpenseService],
})
export class ExpenseModule {}
