import {Button, Stack, Typography} from '@mui/material';
import {observer} from 'mobx-react-lite';
import {useState} from 'react';
import {userStore} from '@/5-entities/user/stores/user-store';
import {eventStore} from '@/5-entities/event/stores/event-store';
import {eventService} from '@/5-entities/event/services/event-service';
import {notificationStore} from '@/6-shared/stores/notification-store';
import {CurrentUserBadge} from '@/6-shared/ui/CurrentUserBadge';
import {PinCodeDisplay} from '@/6-shared/ui/PinCodeDisplay';
import copy from 'copy-to-clipboard';

export const EventHeader = observer(() => {
  const [shouldHidePinCode, setShouldHidePinCode] = useState(true);

  if (!userStore.currentUser || !eventStore.currentEvent) {
    return null;
  }

  const handleChangeUser = () => {
    userStore.setCurrentUser(undefined);
  };

  const handleShareLink = async () => {
    try {
      if (!eventStore.currentEvent) return;

      const {token} = await eventService.createEventShareToken(
        eventStore.currentEvent.id,
        eventStore.currentEvent.pinCode,
      );

      const location = window.location;
      const shareUrl = `${location.origin}/event/${eventStore.currentEvent.id}?token=${token}`;

      copy(shareUrl);

      notificationStore.setNotification(
        'Ссылка скопирована! Не делитесь ссылкой с посторонними людьми. Ссылка будет активна 14 дней.',
        'success',
      );
    } catch (error) {
      console.error('Failed to create share token:', error);
    }
  };

  return (
    <>
      <CurrentUserBadge letter={userStore.currentUser.name[0]} onClick={handleChangeUser} />

      <Typography variant="h3" align="center" marginBottom={'16px'}>
        {eventStore.currentEvent.name}

        <PinCodeDisplay
          pinCode={eventStore.currentEvent.pinCode}
          hidden={shouldHidePinCode}
          onToggle={() => setShouldHidePinCode(!shouldHidePinCode)}
        />
      </Typography>

      <Stack direction={'row'} justifyContent={'center'} marginBottom={'16px'}>
        <Button variant="outlined" onClick={handleShareLink}>
          Скопировать ссылку на поездку
        </Button>
      </Stack>
    </>
  );
});
