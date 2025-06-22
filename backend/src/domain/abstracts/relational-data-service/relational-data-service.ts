import {IRelationalDataService} from '#domain/abstracts/relational-data-service/types';
import {EventRepositoryAbstract} from '#domain/abstracts/relational-data-service/repositories/event.repository';
import {UserRepositoryAbstract} from '#domain/abstracts/relational-data-service/repositories/user.repository';
import {CurrencyRepositoryAbstract} from '#domain/abstracts/relational-data-service/repositories/currency.repository';
import {ExpenseRepositoryAbstract} from '#domain/abstracts/relational-data-service/repositories/expense.repository';
import {CurrencyRateRepositoryAbstract} from '#domain/abstracts/relational-data-service/repositories/currency-rate.repository';

export abstract class RelationalDataServiceAbstract implements IRelationalDataService {
  abstract event: EventRepositoryAbstract;
  abstract user: UserRepositoryAbstract;
  abstract currency: CurrencyRepositoryAbstract;
  abstract expense: ExpenseRepositoryAbstract;
  abstract currencyRate: CurrencyRateRepositoryAbstract;

  abstract initialize: IRelationalDataService['initialize'];
  abstract transaction: IRelationalDataService['transaction'];
  abstract destroy: IRelationalDataService['destroy'];
  abstract flush: IRelationalDataService['flush'];
}
