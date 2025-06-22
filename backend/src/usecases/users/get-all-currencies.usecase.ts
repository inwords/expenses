import {UseCase} from '#packages/use-case';
import {Injectable} from '@nestjs/common';
import {ICurrency} from '#domain/entities/currency.entity';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {CURRENCIES_LIST} from '../../constants';

@Injectable()
export class GetAllCurrenciesUseCase implements UseCase<void, Array<ICurrency>> {
  constructor(private readonly rDataService: RelationalDataServiceAbstract) {}

  public async execute() {
    const [currencies] = await this.rDataService.currency.findAll({limit: CURRENCIES_LIST.length});

    return currencies;
  }
}
