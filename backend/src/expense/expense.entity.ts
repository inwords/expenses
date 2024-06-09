import {Column, Entity, JoinColumn, ManyToOne, PrimaryGeneratedColumn} from 'typeorm';
import {Currency} from '../currency/currency.entity';
import {SplitInfo} from './types';
import {Event} from "../event/event.entity";

@Entity('expense')
export class Expense {
  @PrimaryGeneratedColumn()
  id: string;

  @Column()
  name: string;

  @Column('decimal', {precision: 10, scale: 2})
  amount: number;

  @Column()
  userWhoPaid: string;

  @Column()
  currencyId: string;

  @Column()
  eventId: string;

  @Column({type: 'jsonb'})
  splitInformation: string | Array<SplitInfo>;

  @ManyToOne(() => Currency)
  @JoinColumn({name: 'currency_id'})
  currency: Currency;

  @ManyToOne(() => Event)
  @JoinColumn({name: 'event_id'})
  event: Event;
}
