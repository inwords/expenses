import {TextFieldElement} from 'react-hook-form-mui';

export const ExpenseAmountInput = () => {
  return <TextFieldElement name={'amount'} label={'Сумма траты'} required />;
};
