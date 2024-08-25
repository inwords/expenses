import {Entity, PrimaryGeneratedColumn, Column} from 'typeorm';

export enum CurrencyCode {
  EUR = 'EUR',
  USD = 'USD',
  RUB = 'RUB',
  JPY = 'JPY',
  TRY = 'TRY',
}

@Entity({name: 'currency'})
export class Currency {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({type: 'enum', enum: CurrencyCode})
  code: CurrencyCode;
}
