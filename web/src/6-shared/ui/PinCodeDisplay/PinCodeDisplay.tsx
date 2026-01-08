import {Stack, Typography} from '@mui/material';

interface Props {
  pinCode: string;
  hidden: boolean;
  onToggle: VoidFunction;
}

export const PinCodeDisplay = ({pinCode, hidden, onToggle}: Props) => {
  return (
    <Stack
      justifyContent={'center'}
      direction={'row'}
      spacing={1}
      style={{cursor: 'pointer'}}
      onClick={onToggle}
    >
      <Typography
        style={{
          userSelect: 'none',
        }}
        variant="subtitle1"
        marginBottom={'20px'}
      >
        Пин-код поездки:
      </Typography>

      <Typography
        variant="subtitle1"
        style={{
          filter: hidden ? 'blur(10px)' : undefined,
          transition: 'all .4s ease',
          userSelect: 'none',
        }}
      >
        {pinCode}
      </Typography>
    </Stack>
  );
};
