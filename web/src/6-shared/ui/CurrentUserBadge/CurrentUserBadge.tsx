import {Avatar, IconButton, Stack} from '@mui/material';

interface Props {
  letter: string;
  onClick?: VoidFunction;
}

export const CurrentUserBadge = ({letter, onClick}: Props) => {
  return (
    <Stack alignItems={'end'}>
      <IconButton onClick={onClick}>
        <Avatar variant="rounded" sizes={'xl'}>
          {letter}
        </Avatar>
      </IconButton>
    </Stack>
  );
};
