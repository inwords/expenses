import {HttpException, HttpStatus, Injectable} from '@nestjs/common';
import {UseCase} from '#packages/use-case';

import {getCurrentDateWithoutTime, getDateWithoutTimeWithMoscowTimezone} from '#packages/date-utils';

import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {IExpense, ISplitInfo} from '#domain/entities/expense.entity';
import {CurrencyRateServiceAbstract} from '#domain/abstracts/currency-rate-service/currency-rate-service';
import {ExpenseValueObject} from '#domain/value-objects/expense.value-object';
import {CurrencyRateValueObject} from '#domain/value-objects/currency-rate.value-object';

type Input = Omit<IExpense, 'createdAt' | 'id' | 'updatedAt'> & Partial<Pick<IExpense, 'createdAt'>>;
type Output = IExpense;

@Injectable()
export class SaveEventExpenseUseCase implements UseCase<Input, Output> {
  constructor(
    private readonly rDataService: RelationalDataServiceAbstract,
    private readonly currencyRateService: CurrencyRateServiceAbstract,
  ) {}

  public async execute(input: Input) {
    const [event] = await this.rDataService.event.findById(input.eventId);

    if (!event) {
      throw new HttpException(
        {
          status: HttpStatus.NOT_FOUND,
          error: `Event with id ${input.eventId} not found`,
        },
        HttpStatus.NOT_FOUND,
      );
    }

    return this.rDataService.transaction(async (ctx) => {
      if (event.currencyId === input.currencyId) {
        let splitInformation: ISplitInfo[] = [];

        for (let i of input.splitInformation) {
          splitInformation.push({
            ...i,
            exchangedAmount: i.amount,
          });
        }

        const expense = new ExpenseValueObject({...input, splitInformation}).value;

        await this.rDataService.expense.insert(expense, ctx);

        return expense;
      } else {
        // TODO cкорее всего стоит переделать на один запрос
        const [expenseCurrencyCode] = await this.rDataService.currency.findById(input.currencyId, ctx);
        const [eventCurrencyCode] = await this.rDataService.currency.findById(event.currencyId, ctx);

        if (!eventCurrencyCode || !expenseCurrencyCode) {
          throw new HttpException(
            {
              status: HttpStatus.BAD_REQUEST,
              error: `Wrong Currency Id`,
            },
            HttpStatus.BAD_REQUEST,
          );
        }

        if (expenseCurrencyCode && eventCurrencyCode) {
          const date = getCurrentDateWithoutTime();
          const getDateForExchangeRate = input.createdAt
            ? getDateWithoutTimeWithMoscowTimezone(new Date(input.createdAt))
            : date;

          let [currencyRate] = await this.rDataService.currencyRate.findByDate(getDateForExchangeRate, ctx);

          if (!currencyRate) {
            currencyRate = new CurrencyRateValueObject({
              date,
              rate: await this.currencyRateService.getCurrencyRate(getDateForExchangeRate),
            }).value;

            await this.rDataService.currencyRate.insert(currencyRate, ctx);
          }

          if (currencyRate.rate) {
            const exchangeRate =
              currencyRate.rate[eventCurrencyCode.code] / currencyRate.rate[expenseCurrencyCode.code];

            let splitInformation: ISplitInfo[] = [];

            for (let i of input.splitInformation) {
              splitInformation.push({
                ...i,
                exchangedAmount: Number(Number(i.amount * exchangeRate).toFixed(2)),
              });
            }

            const expense = new ExpenseValueObject({...input, splitInformation}).value;

            await this.rDataService.expense.insert(expense, ctx);

            return expense;
          } else {
            // https://www.youtube.com/watch?v=WR0Uh3-AVNA
            throw new HttpException(
              {
                status: HttpStatus.INTERNAL_SERVER_ERROR,
                error: `No currency rate available for ${getDateWithoutTimeWithMoscowTimezone(new Date(input.createdAt))} date`,
              },
              HttpStatus.INTERNAL_SERVER_ERROR,
            );
          }
        }
      }
    });
  }
}
