import {Transaction} from 'typeorm';

export interface Query<Input, Output> {
  execute: (input: Input, trx?: Transaction) => Promise<Output>;
}
