import {BaseRepository} from '#frameworks/relational-data-service/postgres/repositories/base.repository';
import {DataSource, EntityManager, Repository} from 'typeorm';
import {IQueryDetails} from '#domain/abstracts/relational-data-service/types';
import {CurrencyRepositoryAbstract} from '#domain/abstracts/relational-data-service/repositories/currency.repository';
import {ICurrency} from '#domain/entities/currency.entity';
import {CurrencyEntity} from '#frameworks/relational-data-service/postgres/entities/currency.entity';

export class CurrencyRepository extends BaseRepository implements CurrencyRepositoryAbstract {
  readonly dataSource: DataSource;

  private readonly queryName = 'currency';

  constructor({dataSource, showQueryDetails}: {dataSource: DataSource; showQueryDetails: boolean}) {
    super(showQueryDetails);
    this.dataSource = dataSource;
  }

  readonly findById: CurrencyRepositoryAbstract['findById'] = async (
    id: ICurrency['id'],
    trx,
  ): Promise<[result: ICurrency | null, queryDetails: IQueryDetails]> => {
    const ctx = trx.ctx instanceof EntityManager ? trx.ctx : undefined;

    let query = this.getRepository(ctx).createQueryBuilder(this.queryName);

    query = query.where(`${this.queryName}.id = :id`, {
      id,
    });

    const queryDetails = this.getQueryDetails(query);
    const result = await query.getOne();

    return [result, queryDetails];
  };

  public findAll: CurrencyRepositoryAbstract['findAll'] = async (input, trx) => {
    const {limit} = input;
    const ctx = trx instanceof EntityManager ? trx : undefined;

    let query = this.getRepository(ctx).createQueryBuilder(this.queryName);

    query = query.limit(limit);

    const queryDetails = this.getQueryDetails(query);
    const result = await query.getMany();

    return [result, queryDetails];
  };

  readonly insert: CurrencyRepositoryAbstract['insert'] = async (
    input: ICurrency | ICurrency[],
    trx,
  ): Promise<[result: undefined, queryDetails: IQueryDetails]> => {
    const ctx = trx instanceof EntityManager ? trx : undefined;

    const query = this.getRepository(ctx).createQueryBuilder().insert().values(input);
    const queryDetails = this.getQueryDetails(query);

    await query.execute();

    return [undefined, queryDetails];
  };

  private readonly getRepository = (manager?: EntityManager): Repository<CurrencyEntity> => {
    return manager != null ? manager.getRepository(CurrencyEntity) : this.dataSource.getRepository(CurrencyEntity);
  };
}
