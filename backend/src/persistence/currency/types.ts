import {Query} from "../../packages/query";
import {Currency} from "../../domain/currency/types";

export type FindCurrencyInput = {currencyId: number};
export type IFindCurrency = Query<FindCurrencyInput, Currency>;
