import {TextFieldElement} from 'react-hook-form-mui';

export const ExpenseDescriptionInput = () => {
  return <TextFieldElement name={'description'} label={'Описание траты'} required />;
};
