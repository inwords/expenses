import {Entity, PrimaryGeneratedColumn, Column} from 'typeorm';

enum CurrencyType {
  EUR = 'EUR',
  USD = 'USD',
  RUB = 'RUB',
}

@Entity({name: 'currency'})
export class Currency {
  @PrimaryGeneratedColumn()
  id: string;

  @Column({type: 'enum', enum: CurrencyType})
  type: CurrencyType;
}
