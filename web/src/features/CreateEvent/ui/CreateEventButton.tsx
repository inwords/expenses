import {useContent} from '@/shared/i18n/useContent';
import {Button} from '@mui/material';

interface Props {
  onClick: VoidFunction;
}

export const CreateEventButton = ({onClick}: Props) => {
  const {btn} = useContent().CreateEventButton;

  return (
    <Button
      variant="outlined"
      onClick={() => {
        onClick();
      }}
    >
      {btn}
    </Button>
  );
};
