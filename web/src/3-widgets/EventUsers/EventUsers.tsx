import {userStore} from '@/5-entities/user/stores/user-store';
import {Box, Button, Stack} from '@mui/material';
import {AddUsersToEvent} from '@/4-features/AddUsersToEvent/ui/AddUsersToEvent';
import {observer} from 'mobx-react-lite';
import {Delete} from '@mui/icons-material';
import {userService} from '@/5-entities/user/services/user-service';
import copy from 'copy-to-clipboard';
import {eventStore} from '@/5-entities/event/stores/event-store';

export const EventUsers = observer(() => {
  return (
    <Box padding={'16px'}>
      <Stack direction={'row'} justifyContent={'end'} spacing={2}>
        <AddUsersToEvent />

        <Button
          variant="outlined"
          onClick={() => {
            const location = window.location;
            copy(`${location.origin}${location.pathname}?pinCode=${eventStore.currentEvent?.pinCode}`);
          }}
        >
          Скопировать ссылку на поездку
        </Button>
      </Stack>

      {userStore.users.map((u) => {
        return (
          <Stack key={u.id} direction={'row'} spacing={4}>
            <p>{u.name}</p>

            <Delete
              onClick={() => {
                void userService.deleteUsersFromEvent(u.id);
              }}
            />
          </Stack>
        );
      })}
    </Box>
  );
});
