import {Column, Entity, JoinColumn, ManyToOne, PrimaryGeneratedColumn} from 'typeorm';
import {Event} from '#persistence/entities/event.entity';

@Entity('user')
export class User {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  name: string;

  @Column()
  eventId: number;

  @ManyToOne(() => Event)
  @JoinColumn({name: 'event_id'})
  event: Event;
}
