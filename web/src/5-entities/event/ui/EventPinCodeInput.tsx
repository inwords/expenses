import {TextFieldElement} from 'react-hook-form-mui';

export const EventPinCodeInput = () => {
  return <TextFieldElement name={'pinCode'} label={'Pin Code поездки. Понадобится для доступа к поездке'} required />;
};
