import {Fab, Stack} from '@mui/material';

interface Props {
  setIsOpen: (status: boolean) => void;
}

export const CreateExpense = ({setIsOpen}: Props) => {
  return (
    <Stack justifyContent={'end'} direction={'row'} marginTop={'20px'}>
      <Fab color="primary" onClick={() => setIsOpen(true)}>
        <span className="material-icons-outlined">+</span>
      </Fab>
    </Stack>
  );
};
