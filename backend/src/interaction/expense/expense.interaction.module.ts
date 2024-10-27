import {Module} from '@nestjs/common';
import {GetEventExpenses} from './use-cases/get-event-expenses';
import {ExpensePersistenceModule} from '../../persistence/expense/expense.persistence.module';
import {SaveEventExpense} from './use-cases/save-event-expense';
import {EventPersistenceModule} from '../../persistence/event/event.persistence.module';
import {CurrencyPersistenceModule} from '../../persistence/currency/currency.persistence.module';
import {CurrencyRatePersistenceModule} from "../../persistence/currency-rate/currency-rate.persistence.module";

const useCases = [GetEventExpenses, SaveEventExpense];

@Module({
  imports: [ExpensePersistenceModule, EventPersistenceModule, CurrencyPersistenceModule, CurrencyRatePersistenceModule],
  providers: [...useCases],
  exports: [...useCases],
})
export class ExpenseInteractionModule {}
