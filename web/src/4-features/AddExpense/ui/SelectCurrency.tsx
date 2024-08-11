import {SelectElement} from 'react-hook-form-mui';

export const SelectCurrency = () => {
  return (
    <SelectElement
      label="Валюта"
      name="currencyId"
      options={[
        {
          id: 2,
          label: 'EUR',
        },
        {
          id: 1,
          label: 'USD',
        },
        {
          id: 3,
          label: 'RUB',
        },
      ]}
    />
  );
};
