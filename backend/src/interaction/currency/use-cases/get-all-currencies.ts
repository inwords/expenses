import {UseCase} from '#packages/use-case';
import {Currency} from '#domain/currency/types';
import {IFIndCurrencies} from '#persistence/currency/types';
import {Inject} from '@nestjs/common';
import {FindCurrencies} from '#persistence/currency/queries/find-currencies';

export class GetAllCurrencies implements UseCase<void, Array<Currency>> {
  constructor(
    @Inject(FindCurrencies)
    private readonly findCurrencies: IFIndCurrencies,
  ) {}

  public async execute() {
    return await this.findCurrencies.execute();
  }
}
