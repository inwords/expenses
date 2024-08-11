import {RadioButtonGroup} from 'react-hook-form-mui';
import {expenseService} from '@/5-entities/expense/services/expense-service';

export const SplitOptions = () => {
  return (
    <RadioButtonGroup
      label="Варинт деления траты"
      name="splitOption"
      row
      onChange={(v) => {
        expenseService.setSplitOption(v as '1' | '2');
      }}
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
