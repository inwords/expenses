import {UseCase} from '#packages/use-case';
import {Injectable} from '@nestjs/common';
import {ICurrency} from '#domain/entities/currency.entity';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {CURRENCIES_LIST} from '../../constants';
import {Result, success} from '#packages/result';

@Injectable()
export class GetAllCurrenciesUseCase implements UseCase<void, Result<Array<ICurrency>, never>> {
  constructor(private readonly rDataService: RelationalDataServiceAbstract) {}

  public async execute(): Promise<Result<Array<ICurrency>, never>> {
    const [currencies] = await this.rDataService.currency.findAll({limit: CURRENCIES_LIST.length});

    return success(currencies);
  }
}
