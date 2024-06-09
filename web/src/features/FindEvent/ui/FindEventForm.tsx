import {FormContainer} from 'react-hook-form-mui';
import {EventInput} from '@/features/FindEvent/ui/EventInput';
import {FindEventButton} from '@/features/FindEvent/ui/FindEventButton';
import {Box, Stack} from '@mui/material';
import {EventPinCodeInput} from '@/entities/event/ui/EventPinCodeInput';
import {getEventInfo} from '@/entities/event/services/api';

export const FindEventForm = () => {
  return (
    <Box display="flex" justifyContent="center" marginTop={'20px'}>
      <FormContainer onSuccess={(d) => getEventInfo(d.eventId, {pinCode: d.pinCode})}>
        <Stack direction="column" spacing={2} minWidth={300}>
          <EventInput />

          <EventPinCodeInput />

          <FindEventButton />
        </Stack>
      </FormContainer>
    </Box>
  );
};
