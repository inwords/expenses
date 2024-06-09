import {Column, Entity, JoinColumn, ManyToOne, PrimaryGeneratedColumn} from 'typeorm';
import {Currency} from '../currency/currency.entity';
import {SplitInfo} from './types';

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

  @Column({type: 'jsonb'})
  splitInformation: string | Array<SplitInfo>;

  @ManyToOne(() => Currency)
  @JoinColumn({name: 'currency_id'})
  currency: Currency;
}
