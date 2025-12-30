import {Injectable} from '@nestjs/common';
import {UseCase} from '#packages/use-case';

import {getCurrentDateWithoutTimeUTC, getDateWithoutTimeUTC} from '#packages/date-utils';

import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {EventServiceAbstract} from '#domain/abstracts/event-service/event-service';
import {IExpense, ISplitInfo} from '#domain/entities/expense.entity';
import {ExpenseValueObject} from '#domain/value-objects/expense.value-object';
import {BusinessError} from '#domain/errors/business.error';
import {BUSINESS_ERRORS} from '#domain/errors/business-errors.const';
import {ErrorCode} from '#domain/errors/error-codes.enum';

type Input = Omit<IExpense, 'createdAt' | 'id' | 'updatedAt'> &
  Partial<Pick<IExpense, 'createdAt'>> & {pinCode: string};
type Output = IExpense;

@Injectable()
export class SaveEventExpenseV2UseCase implements UseCase<Input, Output> {
  constructor(
    private readonly rDataService: RelationalDataServiceAbstract,
    private readonly eventService: EventServiceAbstract,
  ) {}

  public async execute(input: Input) {
    return this.rDataService.transaction(async (ctx) => {
      // Блокируем event с pessimistic_write для предотвращения race condition
      const [event] = await this.rDataService.event.findById(input.eventId, {
        ctx,
        lock: 'pessimistic_write',
        onLocked: 'nowait',
      });

      this.eventService.validateEvent(event, input.pinCode);

      if (event.currencyId === input.currencyId) {
        let splitInformation: ISplitInfo[] = [];

        for (let i of input.splitInformation) {
          splitInformation.push({
            ...i,
            exchangedAmount: i.amount,
          });
        }

        const expense = new ExpenseValueObject({...input, splitInformation}).value;

        await this.rDataService.expense.insert(expense, {ctx});

        return expense;
      } else {
        // TODO cкорее всего стоит переделать на один запрос
        const [expenseCurrencyCode] = await this.rDataService.currency.findById(input.currencyId, {ctx});
        const [eventCurrencyCode] = await this.rDataService.currency.findById(event.currencyId, {ctx});

        if (!eventCurrencyCode || !expenseCurrencyCode) {
          throw new BusinessError(BUSINESS_ERRORS[ErrorCode.CURRENCY_NOT_FOUND], {
            eventCurrencyId: event.currencyId,
            expenseCurrencyId: input.currencyId,
          });
        }

        if (expenseCurrencyCode && eventCurrencyCode) {
          const getDateForExchangeRate = input.createdAt
            ? getDateWithoutTimeUTC(new Date(input.createdAt))
            : getCurrentDateWithoutTimeUTC();

          const [currencyRate] = await this.rDataService.currencyRate.findByDate(getDateForExchangeRate, {ctx});

          if (!currencyRate) {
            throw new BusinessError(
              BUSINESS_ERRORS[ErrorCode.CURRENCY_RATE_NOT_FOUND],
              {date: getDateForExchangeRate},
              `Currency rate not found for ${getDateForExchangeRate} date. Please try again later or contact support.`,
            );
          }

          const exchangeRate = currencyRate.rate[eventCurrencyCode.code] / currencyRate.rate[expenseCurrencyCode.code];

          let splitInformation: ISplitInfo[] = [];

          for (let i of input.splitInformation) {
            splitInformation.push({
              ...i,
              exchangedAmount: Number(Number(i.amount * exchangeRate).toFixed(2)),
            });
          }

          const expense = new ExpenseValueObject({...input, splitInformation}).value;

          await this.rDataService.expense.insert(expense, {ctx});

          return expense;
        }
      }
    });
  }
}
