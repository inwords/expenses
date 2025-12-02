import {DataSource, EntityManager, Repository} from 'typeorm';
import {BaseRepository} from './base.repository';
import {DeletedEventRepositoryAbstract} from '#domain/abstracts/relational-data-service/repositories/deleted-event.repository';
import {IQueryDetails} from '#domain/abstracts/relational-data-service/types';
import {IDeletedEvent} from '#domain/entities/deleted-event.entity';
import {DeletedEventEntity} from '#frameworks/relational-data-service/postgres/entities/deleted-event.entity';

export class DeletedEventRepository extends BaseRepository implements DeletedEventRepositoryAbstract {
  readonly dataSource: DataSource;

  private readonly queryName = 'deletedEvent';

  constructor({dataSource, showQueryDetails}: {dataSource: DataSource; showQueryDetails: boolean}) {
    super(showQueryDetails);
    this.dataSource = dataSource;
  }

  readonly findByEventId: DeletedEventRepositoryAbstract['findByEventId'] = async (
    eventId: IDeletedEvent['eventId'],
    trx,
  ): Promise<[result: IDeletedEvent | null, queryDetails: IQueryDetails]> => {
    const ctx = trx?.ctx instanceof EntityManager ? trx.ctx : undefined;

    let query = this.getRepository(ctx).createQueryBuilder(this.queryName);

    query = query.where(`${this.queryName}.eventId = :eventId`, {eventId});

    const queryDetails = this.getQueryDetails(query);
    const result = await query.getOne();

    return [result, queryDetails];
  };

  readonly insert: DeletedEventRepositoryAbstract['insert'] = async (
    deletedEvent: IDeletedEvent,
    trx,
  ): Promise<[result: undefined, queryDetails: IQueryDetails]> => {
    const ctx = trx?.ctx instanceof EntityManager ? trx.ctx : undefined;

    const query = this.getRepository(ctx).createQueryBuilder().insert().values(deletedEvent);
    const queryDetails = this.getQueryDetails(query);

    await query.execute();

    return [undefined, queryDetails];
  };

  private readonly getRepository = (manager?: EntityManager): Repository<DeletedEventEntity> => {
    return manager != null
      ? manager.getRepository(DeletedEventEntity)
      : this.dataSource.getRepository(DeletedEventEntity);
  };
}
