import {Module} from '@nestjs/common';
import {GetEventExpenses} from './use-cases/get-event-expenses';
import {ExpensePersistenceModule} from '../../persistence/expense/expense.persistence.module';
import {SaveEventExpense} from './use-cases/save-event-expense';

const useCases = [GetEventExpenses, SaveEventExpense];

@Module({
  imports: [ExpensePersistenceModule],
  providers: [...useCases],
  exports: [...useCases],
})
export class ExpenseInteractionModule {}
