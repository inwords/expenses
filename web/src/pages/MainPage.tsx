import {FindEventForm} from '@/widgets/FindEventForm/FindEventForm';
import {CreateEventButton} from '@/features/CreateEvent/ui/CreateEventButton';
import {Box, Stack} from '@mui/material';

export const MainPage = () => {
  return (
    <>
      <FindEventForm />

      <Box display="flex" justifyContent="center" marginTop={'16px'}>
        <Stack minWidth={300}>
          <CreateEventButton />
        </Stack>
      </Box>
    </>
  );
};
