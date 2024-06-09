import {Column, Entity, ManyToOne, PrimaryGeneratedColumn} from 'typeorm';
import {Currency} from '../currency/currency.entity';
import {JoinColumn} from 'typeorm';
import {User} from './types';

@Entity({name: 'event'})
export class Event {
  @PrimaryGeneratedColumn()
  id: string;

  @Column()
  name: string;

  @Column()
  ownerId: string;

  @Column()
  currencyId: string;

  @Column({type: 'jsonb'})
  users: string | Array<User>;

  @ManyToOne(() => Currency)
  @JoinColumn({name: 'currency_id'})
  currency: Currency;
}
