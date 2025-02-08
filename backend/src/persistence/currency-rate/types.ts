import {Query} from '#packages/query';
import {CurrencyRate} from '#domain/currency-rate/types';
import {DateWithoutTime} from "#packages/date-utils";

export type FindCurrencyRateInput = {date: string};
export type IFindCurrencyRate = Query<FindCurrencyRateInput, CurrencyRate | null>;
export type UpsertCurrencyRateInput = {rate: Record<string, number>; date: string};
export type IUpsertCurrencyRate = Query<UpsertCurrencyRateInput, void>;
export type IGetCurrencyRate = Query<DateWithoutTime, Record<string, number> | null>;
