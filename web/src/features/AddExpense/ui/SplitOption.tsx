import {CheckboxButtonGroup} from 'react-hook-form-mui';

export const SplitOptions = () => {
  return (
    <CheckboxButtonGroup
      label="Варинт деления траты"
      name="splitOption"
      row
      options={[
        {
          id: '1',
          label: 'Поровну',
        },
        {
          id: '2',
          label: 'Внести вручную',
        },
      ]}
    />
  );
};
