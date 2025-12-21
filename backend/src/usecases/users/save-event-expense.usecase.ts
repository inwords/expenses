import {HttpException, HttpStatus, Injectable} from '@nestjs/common';
import {UseCase} from '#packages/use-case';

import {getCurrentDateWithoutTimeUTC, getDateWithoutTimeUTC} from '#packages/date-utils';

import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {IExpense, ISplitInfo} from '#domain/entities/expense.entity';
import {ExpenseValueObject} from '#domain/value-objects/expense.value-object';

type Input = Omit<IExpense, 'createdAt' | 'id' | 'updatedAt'> & Partial<Pick<IExpense, 'createdAt'>>;
type Output = IExpense;

@Injectable()
export class SaveEventExpenseUseCase implements UseCase<Input, Output> {
  constructor(private readonly rDataService: RelationalDataServiceAbstract) {}

  public async execute(input: Input) {
    return this.rDataService.transaction(async (ctx) => {
      // Блокируем event с pessimistic_write для предотвращения race condition
      // Если кто-то пытается удалить event, мы заблокируем строку и проверим что она не удалена
      const [event] = await this.rDataService.event.findById(input.eventId, {
        ctx,
        lock: 'pessimistic_write',
        onLocked: 'nowait',
      });

      if (!event) {
        throw new HttpException(
          {
            status: HttpStatus.NOT_FOUND,
            error: `Event with id ${input.eventId} not found`,
          },
          HttpStatus.NOT_FOUND,
        );
      }

      // Проверяем что event не был soft deleted
      if (event.deletedAt !== null) {
        throw new HttpException(
          {
            status: HttpStatus.GONE,
            error: `Event with id ${input.eventId} has been deleted`,
          },
          HttpStatus.GONE,
        );
      }

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
          throw new HttpException(
            {
              status: HttpStatus.BAD_REQUEST,
              error: `Wrong Currency Id`,
            },
            HttpStatus.BAD_REQUEST,
          );
        }

        if (expenseCurrencyCode && eventCurrencyCode) {
          const getDateForExchangeRate = input.createdAt
            ? getDateWithoutTimeUTC(new Date(input.createdAt))
            : getCurrentDateWithoutTimeUTC();

          const [currencyRate] = await this.rDataService.currencyRate.findByDate(getDateForExchangeRate, {ctx});

          if (!currencyRate) {
            throw new HttpException(
              {
                status: HttpStatus.NOT_FOUND,
                error: `Currency rate not found for ${getDateForExchangeRate} date. Please try again later or contact support.`,
              },
              HttpStatus.NOT_FOUND,
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
