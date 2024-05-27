import {Column, Entity, ManyToOne, PrimaryGeneratedColumn} from 'typeorm';
import {Currency} from "../currency/currency.entity";

@Entity({name: 'event'})
export class Event {
  @PrimaryGeneratedColumn()
  id: string;

  @Column()
  name: string;

  @Column()
  owner: string;

  @ManyToOne(() => Currency, currency => currency.id)
  currency: Currency;
}
