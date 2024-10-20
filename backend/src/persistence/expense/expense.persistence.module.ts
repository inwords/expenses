import {Module} from '@nestjs/common';
import {FindExpenses} from './queries/find-expenses';
import {UpsertExpense} from './queries/upsert-expense';

const queries = [FindExpenses, UpsertExpense];

@Module({
  providers: [...queries],
  exports: [...queries],
})
export class ExpensePersistenceModule {}
