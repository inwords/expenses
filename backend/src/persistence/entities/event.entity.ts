import {Column, Entity, ManyToOne, PrimaryGeneratedColumn} from 'typeorm';
import {JoinColumn} from 'typeorm';
import {Currency} from "#persistence/entities/currency.entity";

@Entity({name: 'event'})
export class Event {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  name: string;

  @Column()
  currencyId: number;

  @Column()
  pinCode: string;

  @ManyToOne(() => Currency)
  @JoinColumn({name: 'currency_id'})
  currency: Currency;
}
