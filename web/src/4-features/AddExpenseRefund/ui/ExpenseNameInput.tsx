import {TextFieldElement} from 'react-hook-form-mui';

export const ExpenseRefundDescriptionInput = () => {
  return <TextFieldElement name={'description'} label={'Описание возврата'} required />;
};
