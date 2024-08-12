import {CreateEventButton} from '@/4-features/CreateEvent/ui/CreateEventButton';
import {Box, Stack} from '@mui/material';
import {useEffect, useState} from 'react';
import {CreateEventModal} from '@/3-widgets/CreateEventModal/CreateEventModal';
import {FindEventForm} from '@/4-features/FindEvent/ui/FindEventForm';
import {currencyService} from '@/5-entities/currency/services/currency-service';

export const MainPage = () => {
  const [isDialogOpen, setIsDialogOpen] = useState(false);

  useEffect(() => {
    void currencyService.fetchCurrencies();
  }, []);

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
