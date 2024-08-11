import {Column, CreateDateColumn, Entity, JoinColumn, ManyToMany, ManyToOne, PrimaryGeneratedColumn} from 'typeorm';
import {Currency} from '../currency/currency.entity';
import {SplitInfo} from './types';
import {Event} from '../event/event.entity';
import {User} from '../user/user.entity';

@Entity('expense')
export class Expense {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  description: string;

  @Column()
  userWhoPaidId: number;

  @Column()
  currencyId: number;

  @Column()
  eventId: number;

  @Column({type: 'jsonb'})
  splitInformation: string | Array<SplitInfo>;

  @CreateDateColumn({type: 'timestamptz'})
  createdAt!: Date;

  @ManyToOne(() => Currency)
  @JoinColumn({name: 'currency_id'})
  currency: Currency;

  @ManyToOne(() => Event)
  @JoinColumn({name: 'event_id'})
  event: Event;

  @ManyToMany(() => User)
  @JoinColumn({name: 'user_who_paid_id'})
  user: User;
}
