import {Avatar, IconButton} from '@mui/material';

interface Props {
  letter: string;
  onClick: VoidFunction;
  isSelected: boolean;
}

export const UserAvatar = ({onClick, letter, isSelected}: Props) => {
  return (
    <IconButton onClick={onClick}>
      <Avatar variant="rounded" style={{border: isSelected ? '2px solid red' : undefined}} sizes={'xl'}>
        {letter}
      </Avatar>
    </IconButton>
  );
};
