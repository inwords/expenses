import {Typography} from '@mui/material';
import {observer} from 'mobx-react-lite';
import {useState} from 'react';
import {userStore} from '@/5-entities/user/stores/user-store';
import {eventStore} from '@/5-entities/event/stores/event-store';
import {CurrentUserBadge} from '@/6-shared/ui/CurrentUserBadge';
import {PinCodeDisplay} from '@/6-shared/ui/PinCodeDisplay';

export const EventHeader = observer(() => {
  const [shouldHidePinCode, setShouldHidePinCode] = useState(true);

  if (!userStore.currentUser || !eventStore.currentEvent) {
    return null;
  }

  const handleChangeUser = () => {
    userStore.setCurrentUser(undefined);
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
    </>
  );
});
