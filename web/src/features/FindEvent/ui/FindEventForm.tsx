import {FormContainer} from 'react-hook-form-mui';
import {EventInput} from '@/features/FindEvent/ui/EventInput';
import {FindEventButton} from '@/features/FindEvent/ui/FindEventButton';
import {Box, Stack} from '@mui/material';
import {EventPinCodeInput} from '@/entities/event/ui/EventPinCodeInput';
import {eventService} from '@/entities/event/services/event-service';
import {useNavigate} from 'react-router';
import {ROUTES} from '@/shared/routing/constants';

export const FindEventForm = () => {
  const navigate = useNavigate();

  return (
    <Box display="flex" justifyContent="center" marginTop={'20px'}>
      <FormContainer
        onSuccess={async (d) => {
          await eventService.getEventInfo(d.eventId, {pinCode: d.pinCode});

          navigate(ROUTES.Event(d.eventId));
        }}
      >
        <Stack direction="column" spacing={2} minWidth={300}>
          <EventInput />

          <EventPinCodeInput />

          <FindEventButton />
        </Stack>
      </FormContainer>
    </Box>
  );
};
