import {makeAutoObservable} from 'mobx';
import {Currency} from '@/5-entities/currency/types/types';

export class CurrencyStore {
  currencies: Array<Currency> = [];

  constructor() {
    makeAutoObservable(this);
  }

  get currenciesOptions() {
    return this.currencies.map((c) => {
      return {id: c.id, label: c.code};
    });
  }

  getCurrencyCode(currencyId: string | undefined): string {
    if (!currencyId) {
      return '';
    }
    const currency = this.currencies.find((c) => c.id === currencyId);
    return currency?.code || '';
  }

  setCurrencies(currencies: Array<Currency>) {
    this.currencies = currencies;
  }
}

export const currencyStore = new CurrencyStore();
