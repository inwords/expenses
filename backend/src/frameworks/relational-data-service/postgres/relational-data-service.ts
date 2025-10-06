import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {DataSource} from 'typeorm';
import {createTypeormConfigDefault} from './config';
import {EventRepository} from '#frameworks/relational-data-service/postgres/repositories/event.repository';
import {UserRepository} from '#frameworks/relational-data-service/postgres/repositories/user.repository';
import {CurrencyRepository} from '#frameworks/relational-data-service/postgres/repositories/currency.repository';
import {ExpenseRepository} from '#frameworks/relational-data-service/postgres/repositories/expense.repository';
import {CurrencyRateRepository} from '#frameworks/relational-data-service/postgres/repositories/currency-rate.repository';

export class RelationalDataService implements RelationalDataServiceAbstract {
  readonly dbConfig;
  readonly dataSource;
  readonly showQueryDetails;
  readonly transaction;

  readonly event: EventRepository;
  readonly user: UserRepository;
  readonly currency: CurrencyRepository;
  readonly expense: ExpenseRepository;
  readonly currencyRate: CurrencyRateRepository;

  constructor({dbConfig, showQueryDetails}) {
    this.dbConfig = dbConfig;
    this.dataSource = new DataSource(createTypeormConfigDefault(dbConfig));
    this.transaction = this.dataSource.transaction.bind(this.dataSource);
    this.showQueryDetails = showQueryDetails;

    this.event = new EventRepository({dataSource: this.dataSource, showQueryDetails});
    this.user = new UserRepository({dataSource: this.dataSource, showQueryDetails});
    this.currency = new CurrencyRepository({dataSource: this.dataSource, showQueryDetails});
    this.expense = new ExpenseRepository({dataSource: this.dataSource, showQueryDetails});
    this.currencyRate = new CurrencyRateRepository({dataSource: this.dataSource, showQueryDetails});
  }

  async initialize(): Promise<void> {
    await this.dataSource.initialize();
    await this.setupSearchPathAndValidate();
  }

  async destroy(): Promise<void> {
    if (this.dataSource.isInitialized) {
      await this.dataSource.destroy();
    }
  }

  async flush(): Promise<void> {
    const targetEntities = this.dataSource.entityMetadatas;

    const tableNames = targetEntities.map((t) => t.tableName);
    const tableNamesJoined = tableNames.join(', ');
    const truncateSql = `TRUNCATE ${tableNamesJoined} RESTART IDENTITY CASCADE;`;
    await this.dataSource.query(truncateSql);
  }

  private async setupSearchPathAndValidate(): Promise<void | never> {
    const user = this.dbConfig.user;
    const schema = this.dbConfig.schema;

    const [{current_schema: currentSchema}] = await this.dataSource.query(`SELECT current_schema();`);
    if (currentSchema !== schema) {
      throw new Error(`Invalid connection schema for user "${user}". Expected "${schema}", got "${currentSchema}";`);
    }
  }
}
