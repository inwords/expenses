import {BaseRepository} from '#frameworks/relational-data-service/postgres/repositories/base.repository';
import {ExpenseRepositoryAbstract} from '#domain/abstracts/relational-data-service/repositories/expense.repository';
import {DataSource, EntityManager, Repository} from 'typeorm';
import {IQueryDetails} from '#domain/abstracts/relational-data-service/types';
import {IExpense} from '#domain/entities/expense.entity';
import {ExpenseEntity} from '#frameworks/relational-data-service/postgres/entities/expense.entity';

export class ExpenseRepository extends BaseRepository implements ExpenseRepositoryAbstract {
  readonly dataSource: DataSource;

  private readonly queryName = 'expense';

  constructor({dataSource, showQueryDetails}: {dataSource: DataSource; showQueryDetails: boolean}) {
    super(showQueryDetails);
    this.dataSource = dataSource;
  }

  readonly findByEventId: ExpenseRepositoryAbstract['findByEventId'] = async (
    eventId: IExpense['eventId'],
    trx,
  ): Promise<[result: IExpense[] | null, queryDetails: IQueryDetails]> => {
    const ctx = trx.ctx instanceof EntityManager ? trx.ctx : undefined;

    let query = this.getRepository(ctx).createQueryBuilder(this.queryName);

    query = query.where(`${this.queryName}.event_id = :eventId`, {
      eventId,
    });

    const queryDetails = this.getQueryDetails(query);
    const result = await query.getMany();

    return [result, queryDetails];
  };

  readonly insert: ExpenseRepositoryAbstract['insert'] = async (
    input: IExpense,
    trx,
  ): Promise<[result: undefined, queryDetails: IQueryDetails]> => {
    const ctx = trx instanceof EntityManager ? trx : undefined;

    const query = this.getRepository(ctx).createQueryBuilder().insert().values(input);
    const queryDetails = this.getQueryDetails(query);

    await query.execute();

    return [undefined, queryDetails];
  };

  private readonly getRepository = (manager?: EntityManager): Repository<ExpenseEntity> => {
    return manager != null ? manager.getRepository(ExpenseEntity) : this.dataSource.getRepository(ExpenseEntity);
  };
}
