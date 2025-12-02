import {Column, Entity, PrimaryColumn} from 'typeorm';
import {IDeletedEvent} from '#domain/entities/deleted-event.entity';

@Entity({name: 'deleted_event'})
export class DeletedEventEntity implements IDeletedEvent {
  @PrimaryColumn({type: 'varchar'})
  eventId!: IDeletedEvent['eventId'];

  @Column({type: 'timestamptz'})
  deletedAt!: IDeletedEvent['deletedAt'];
}
