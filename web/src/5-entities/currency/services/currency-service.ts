import {getCurrencies} from '@/5-entities/currency/services/api';
import {currencyStore} from '@/5-entities/currency/stores/currency-store';

export class CurrencyService {
  async fetchCurrencies() {
    const currencies = await getCurrencies();

    currencyStore.setCurrencies(currencies);
  }
}

export const currencyService = new CurrencyService();
