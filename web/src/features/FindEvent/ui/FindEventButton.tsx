import {useContent} from '@/shared/i18n/useContent';
import {Button} from '@mui/material';

export const FindEventButton = () => {
  const {btn} = useContent().FindEventButton;

  return (
    <Button type={'submit'} variant="contained">
      {btn}
    </Button>
  );
};
