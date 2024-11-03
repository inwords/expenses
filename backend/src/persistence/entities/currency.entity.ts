import {Entity, PrimaryGeneratedColumn, Column} from 'typeorm';
import {CurrencyCode} from '#domain/currency/constants';

@Entity({name: 'currency'})
export class Currency {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({type: 'enum', enum: CurrencyCode})
  code: CurrencyCode;
}
