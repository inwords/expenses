import {TextFieldElement} from 'react-hook-form-mui';

export const EventInput = () => {
  return <TextFieldElement name={'eventId'} label={'Id поездки'} required />;
};
