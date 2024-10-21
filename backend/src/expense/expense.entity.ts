import {Column, CreateDateColumn, Entity, JoinColumn, ManyToMany, ManyToOne, PrimaryGeneratedColumn} from 'typeorm';
import {Currency} from '../currency/currency.entity';
import {Event} from '../event/event.entity';
import {User} from '../user/user.entity';
import {ExpenseType} from "../domain/expense/constants";
import {SplitInfo} from "../domain/expense/types";

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

  @Column({type: 'enum', enum: ExpenseType})
  expenseType: ExpenseType;

  @Column({type: 'jsonb'})
  splitInformation: Array<SplitInfo>;

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
