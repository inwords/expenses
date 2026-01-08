import {CreateEventButton} from '@/4-features/CreateEvent/ui/CreateEventButton';
import {Box, Stack, Typography, Container} from '@mui/material';
import {useEffect, useState} from 'react';
import {CreateEventModal} from '@/3-widgets/CreateEventModal/CreateEventModal';
import {FindEventForm} from '@/4-features/FindEvent/ui/FindEventForm';
import {currencyService} from '@/5-entities/currency/services/currency-service';
import {OnboardingTour} from '@/3-widgets/OnboardingTour';
import {MAIN_PAGE_ONBOARDING_STEPS} from '@/6-shared/constants/onboarding-steps';

export const MainPage = () => {
  const [isDialogOpen, setIsDialogOpen] = useState(false);

  useEffect(() => {
    void currencyService.fetchCurrencies();
  }, []);

  return (
    <Container maxWidth="md">
      <Box display="flex" flexDirection="column" alignItems="center" paddingTop={4}>
        <Typography variant="h2" component="h1" gutterBottom align="center">
          CommonEx
        </Typography>

        <Typography variant="subtitle1" color="text.secondary" align="center" marginBottom={4}>
          Удобный сервис для учёта общих расходов в поездках и мероприятиях.
          Создавайте события, добавляйте участников и отслеживайте, кто кому должен.
        </Typography>

        <FindEventForm />

        <Box display="flex" justifyContent="center" marginTop={'16px'}>
          <Stack minWidth={300}>
            <CreateEventButton onClick={() => setIsDialogOpen(true)} />
          </Stack>
        </Box>

        <CreateEventModal isOpen={isDialogOpen} setIsOpen={setIsDialogOpen} />
      </Box>

      <OnboardingTour steps={MAIN_PAGE_ONBOARDING_STEPS} />
    </Container>
  );
};
