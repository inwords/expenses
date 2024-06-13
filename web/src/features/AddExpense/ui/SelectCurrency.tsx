import {SelectElement} from 'react-hook-form-mui';

export const SelectCurrency = () => {
  return (
    <SelectElement
      label="Валюта"
      name="currencyId"
      options={[
        {
          id: '1',
          label: 'EUR',
        },
        {
          id: '2',
          label: 'USD',
        },
        {
          id: '3',
          label: 'RUB',
        },
      ]}
    />
  );
};
