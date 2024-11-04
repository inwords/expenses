import {Query} from '#packages/query';
import {CurrencyRate} from '#domain/currency-rate/types';

export type FindCurrencyRateInput = {date: string};
export type IFindCurrencyRate = Query<FindCurrencyRateInput, CurrencyRate | null>;
export type UpsertCurrencyRateInput = {rate: Record<string, number>; date: string};
export type IUpsertCurrencyRate = Query<UpsertCurrencyRateInput, void>;
export type IGetCurrencyRate = Query<void, Record<string, number> | null>;
