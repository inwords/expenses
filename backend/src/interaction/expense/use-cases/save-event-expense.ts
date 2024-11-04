import {Inject, Injectable} from '@nestjs/common';
import {UseCase} from '#packages/use-case';
import {IUpsertExpense, UpsertExpenseInput} from '#persistence/expense/types';
import {Expense, SplitInfo} from '#domain/expense/types';
import {UpsertExpense} from '#persistence/expense/queries/upsert-expense';
import {IFindEvent} from '#persistence/event/types';
import {FindEvent} from '#persistence/event/queries/find-event';
import {FindCurrency} from '#persistence/currency/queries/find-currency';
import {IFindCurrency} from '#persistence/currency/types';
import {IFindCurrencyRate, IGetCurrencyRate, IUpsertCurrencyRate} from '#persistence/currency-rate/types';
import {getCurrentDateWithoutTime} from '#packages/date-utils';
import {FindCurrencyRate} from '#persistence/currency-rate/queries/find-currency-rate';
import {GetCurrencyRate} from '#persistence/currency-rate/queries/get-currency-rate';
import {UpsertCurrencyRate} from '#persistence/currency-rate/queries/upsert-currency-rate';

type Input = Omit<UpsertExpenseInput, 'createdAt'>;
type Output = Expense;

@Injectable()
export class SaveEventExpense implements UseCase<Input, Output> {
  constructor(
    @Inject(UpsertExpense)
    private readonly upsertExpense: IUpsertExpense,
    @Inject(FindEvent)
    private readonly findEvent: IFindEvent,
    @Inject(FindCurrency)
    private readonly findCurrency: IFindCurrency,
    @Inject(FindCurrencyRate)
    private readonly findCurrencyRate: IFindCurrencyRate,
    @Inject(GetCurrencyRate)
    private readonly getCurrencyRate: IGetCurrencyRate,
    @Inject(UpsertCurrencyRate)
    private readonly upsertCurrencyRate: IUpsertCurrencyRate,
  ) {}

  public async execute(input: Input) {
    const event = await this.findEvent.execute({eventId: input.eventId});

    if (event) {
      const createdAt = new Date().toISOString();

      if (event.currencyId === input.currencyId) {
        return this.upsertExpense.execute({...input, createdAt});
      } else {
        const expenseCurrencyCode = await this.findCurrency.execute({currencyId: input.currencyId});
        const eventCurrencyCode = await this.findCurrency.execute({currencyId: event.currencyId});

        if (expenseCurrencyCode && eventCurrencyCode) {
          const date = getCurrentDateWithoutTime();

          let currencyRate = await this.findCurrencyRate.execute({date});

          if (!currencyRate) {
            currencyRate = {date, rate: await this.getCurrencyRate.execute()};
          }

          if (currencyRate.rate) {
            void this.upsertCurrencyRate.execute(currencyRate);

            const exchangeRate =
              currencyRate.rate[eventCurrencyCode.code] / currencyRate.rate[expenseCurrencyCode.code];

            let splitInformation: SplitInfo[] = [];

            for (let i of input.splitInformation) {
              splitInformation.push({
                ...i,
                amount: Number(Number(i.amount * exchangeRate).toFixed(2)),
              });
            }

            return this.upsertExpense.execute({...input, createdAt, splitInformation});
          } else {
            // https://www.youtube.com/watch?v=WR0Uh3-AVNA
          }
        }
      }
    }
  }
}
