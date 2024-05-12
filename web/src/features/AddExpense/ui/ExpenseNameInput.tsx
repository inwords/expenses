import {TextFieldElement} from 'react-hook-form-mui';

export const ExpenseNameInput = () => {
  return <TextFieldElement name={'name'} label={'Название траты'} required />;
};
