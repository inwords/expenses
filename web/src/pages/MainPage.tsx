import {FindEventForm} from '@/widgets/FindEventForm/FindEventForm';
import {CreateEventButton} from '@/features/CreateEvent/ui/CreateEventButton';
import {Box, Stack} from '@mui/material';
import {useState} from 'react';
import {CreateEventModal} from '@/widgets/CreateEventModal/CreateEventModal';

export const MainPage = () => {
  const [isDialogOpen, setIsDialogOpen] = useState(false);

  return (
    <>
      <FindEventForm />

      <Box display="flex" justifyContent="center" marginTop={'16px'}>
        <Stack minWidth={300}>
          <CreateEventButton onClick={() => setIsDialogOpen(true)} />
        </Stack>
      </Box>

      <CreateEventModal isOpen={isDialogOpen} setIsOpen={setIsDialogOpen} />
    </>
  );
};
