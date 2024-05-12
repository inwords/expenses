import {useContent} from '@/shared/i18n/useContent';
import {Button} from '@mui/material';

export const CreateEventButton = () => {
  const {btn} = useContent().CreateEventButton;

  return <Button variant="outlined">{btn}</Button>;
};
