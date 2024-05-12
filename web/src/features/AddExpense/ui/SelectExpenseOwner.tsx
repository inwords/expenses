import {SelectElement} from 'react-hook-form-mui';

export const SelectExpenseOwner = () => {
  return (
    <SelectElement
      label="Кто оплачивал"
      name="owner"
      options={[
        {
          id: '1',
          label: 'Ignat',
        },
        {
          id: '2',
          label: 'Vasya',
        },
        {
          id: '3',
          label: 'Dog',
        },
      ]}
    />
  );
};
