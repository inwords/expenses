import {SelectElement} from 'react-hook-form-mui';
import {currencyStore} from "@/5-entities/currency/stores/currency-store";

export const SelectCurrency = () => {
  return (
    <SelectElement
      label="Валюта"
      name="currencyId"
      options={currencyStore.currenciesOptions}
    />
  );
};
