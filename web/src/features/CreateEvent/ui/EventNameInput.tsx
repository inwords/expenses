import {TextFieldElement} from 'react-hook-form-mui';

export const EventNameInput = () => {
  return <TextFieldElement name={'name'} label={'Название поездки'} required />;
};
