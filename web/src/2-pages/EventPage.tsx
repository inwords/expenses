import {SelectUserList} from '@/3-widgets/SelectUserList/SelectUserList';
import {Stack, Typography} from '@mui/material';
import {EventTabs} from '@/3-widgets/EventTabs/EventTabs';
import {useEffect, useState} from 'react';
import {Navigate, useLocation, useParams} from 'react-router';
import {expenseService} from '@/5-entities/expense/services/expense-service';
import {userStore} from '@/5-entities/user/stores/user-store';
import {observer} from 'mobx-react-lite';
import {eventStore} from '@/5-entities/event/stores/event-store';
import {UserAvatar} from '@/4-features/SelectUser/ui/UserAvatar';
import {ROUTES} from '@/6-shared/routing/constants';
import {useSearchParams} from 'react-router-dom';
import {eventService} from '@/5-entities/event/services/event-service';
import {currencyService} from "@/5-entities/currency/services/currency-service";

export const EventPage = observer(() => {
  const {id} = useParams();
  const location = useLocation();
  const [searchParams] = useSearchParams();
  const pinCode = searchParams.get('pinCode');
  const navigatedFromMainForm = location.state === 'navigateFromMainForm';
  const canOpenPage = id && (pinCode || navigatedFromMainForm);
  const [shouldHidePinCode, setShouldHidePinCode] = useState(true);

  useEffect(() => {
    if (canOpenPage) {
      if (!navigatedFromMainForm && pinCode) {
        void currencyService.fetchCurrencies();
        void eventService.getEventInfo(id, {pinCode});
      }

      void expenseService.fetchExpenses(id);
    }
  }, []);

  if (!canOpenPage) {
    return <Navigate to={ROUTES.Main} />;
  }

  return (
    <>
      {userStore.currentUser && (
        <Stack alignItems={'end'}>
          <UserAvatar letter={userStore.currentUser.name[0]} isSelected />
        </Stack>
      )}

      <SelectUserList />

      {userStore.currentUser && (
        <>
          <Typography variant="h3" align="center" marginBottom={'16px'}>
            {eventStore.currentEvent?.name}

            <Stack
              justifyContent={'center'}
              direction={'row'}
              spacing={1}
              style={{cursor: 'pointer'}}
              onClick={() => setShouldHidePinCode(!shouldHidePinCode)}
            >
              <Typography
                style={{
                  userSelect: 'none',
                }}
                variant="subtitle1"
                marginBottom={'20px'}
              >
                Пин-код поездки:{' '}
              </Typography>

              <Typography
                variant="subtitle1"
                style={{
                  filter: shouldHidePinCode ? 'blur(10px)' : undefined,
                  transition: 'transition: all .4s ease',
                  userSelect: 'none',
                }}
              >
                {eventStore.currentEvent?.pinCode}
              </Typography>
            </Stack>
          </Typography>

          <EventTabs />
        </>
      )}
    </>
  );
});
