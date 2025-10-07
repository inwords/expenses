import {BaseRepository} from '#frameworks/relational-data-service/postgres/repositories/base.repository';
import {UserRepositoryAbstract} from '#domain/abstracts/relational-data-service/repositories/user.repository';
import {DataSource, EntityManager, Repository} from 'typeorm';
import {IQueryDetails} from '#domain/abstracts/relational-data-service/types';
import {IUser} from '#domain/entities/user.enitity';
import {UserEntity} from '#frameworks/relational-data-service/postgres/entities/user.entity';

export class UserRepository extends BaseRepository implements UserRepositoryAbstract {
  readonly dataSource: DataSource;

  private readonly queryName = 'user';

  constructor({dataSource, showQueryDetails}: {dataSource: DataSource; showQueryDetails: boolean}) {
    super(showQueryDetails);
    this.dataSource = dataSource;
  }

  readonly findByEventId: UserRepositoryAbstract['findByEventId'] = async (
    eventId: IUser['eventId'],
    trx,
  ): Promise<[result: IUser[] | null, queryDetails: IQueryDetails]> => {
    const ctx = trx.ctx instanceof EntityManager ? trx.ctx : undefined;

    let query = this.getRepository(ctx).createQueryBuilder(this.queryName);

    query = query.where(`${this.queryName}.event_id = :eventId`, {
      eventId,
    });

    const queryDetails = this.getQueryDetails(query);
    const result = await query.getMany();

    return [result, queryDetails];
  };

  readonly insert: UserRepositoryAbstract['insert'] = async (
    input: IUser | IUser[],
    trx,
  ): Promise<[result: undefined, queryDetails: IQueryDetails]> => {
    const ctx = trx instanceof EntityManager ? trx : undefined;

    const query = this.getRepository(ctx).createQueryBuilder().insert().values(input);
    const queryDetails = this.getQueryDetails(query);

    await query.execute();

    return [undefined, queryDetails];
  };

  private readonly getRepository = (manager?: EntityManager): Repository<UserEntity> => {
    return manager != null ? manager.getRepository(UserEntity) : this.dataSource.getRepository(UserEntity);
  };
}
