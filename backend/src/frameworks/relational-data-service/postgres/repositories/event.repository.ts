import {BaseRepository} from './base.repository';
import {EventRepositoryAbstract} from '#domain/abstracts/relational-data-service/repositories/event.repository';
import {DataSource, EntityManager, Repository} from 'typeorm';
import {IEvent} from '#domain/entities/event.entity';
import {IQueryDetails} from '#domain/abstracts/relational-data-service/types';
import {EventEntity} from '#frameworks/relational-data-service/postgres/entities/event.entity';

export class EventRepository extends BaseRepository implements EventRepositoryAbstract {
  readonly dataSource: DataSource;

  private readonly queryName = 'event';

  constructor({dataSource, showQueryDetails}: {dataSource: DataSource; showQueryDetails: boolean}) {
    super(showQueryDetails);
    this.dataSource = dataSource;
  }

  readonly findById: EventRepositoryAbstract['findById'] = async (
    id: IEvent['id'],
    trx,
  ): Promise<[result: IEvent | null, queryDetails: IQueryDetails]> => {
    const ctx = trx instanceof EntityManager ? trx : undefined;

    let query = this.getRepository(ctx).createQueryBuilder(this.queryName);

    query = query.where(`${this.queryName}.id = :id`, {
      id,
    });

    const queryDetails = this.getQueryDetails(query);
    const result = await query.getOne();

    return [result, queryDetails];
  };

  readonly insert: EventRepositoryAbstract['insert'] = async (
    input: IEvent,
    trx,
  ): Promise<[result: undefined, queryDetails: IQueryDetails]> => {
    const ctx = trx instanceof EntityManager ? trx : undefined;

    const query = this.getRepository(ctx).createQueryBuilder().insert().values(input);
    const queryDetails = this.getQueryDetails(query);

    await query.execute();

    return [undefined, queryDetails];
  };

  private readonly getRepository = (manager?: EntityManager): Repository<EventEntity> => {
    return manager != null ? manager.getRepository(EventEntity) : this.dataSource.getRepository(EventEntity);
  };
}
