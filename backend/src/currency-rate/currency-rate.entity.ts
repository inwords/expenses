import {Column, Entity, PrimaryColumn} from 'typeorm';
import {DateWithoutTime} from "../packages/date-utils";

@Entity('currency_rate')
export class CurrencyRate {
  @PrimaryColumn({
    type: 'date',
    primaryKeyConstraintName: 'pk__currency__rate__date',
  })
  date: DateWithoutTime; //2024-12-31

  @Column({type: 'jsonb'})
  rate: Record<string, number>;
}
