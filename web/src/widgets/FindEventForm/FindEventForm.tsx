import {FormContainer} from 'react-hook-form-mui';
import {EventInput} from '@/features/FindEvent/ui/EventInput';
import {FindEventButton} from '@/features/FindEvent/ui/FindEventButton';
import {Box, Stack} from '@mui/material';
import {EventCodeInput} from '@/features/FindEvent/ui/EvenCodetInput';
import {useNavigate} from 'react-router';
import {ROUTES} from '@/shared/routing/constants';

export const FindEventForm = () => {
  const navigate = useNavigate();

  return (
    <Box display="flex" justifyContent="center" marginTop={'20px'}>
      <FormContainer onSuccess={(d) => navigate(`${ROUTES.Event(d.eventId)}`)}>
        <Stack direction="column" spacing={2} minWidth={300}>
          <EventInput />

          <EventCodeInput />

          <FindEventButton />
        </Stack>
      </FormContainer>
    </Box>
  );
};
