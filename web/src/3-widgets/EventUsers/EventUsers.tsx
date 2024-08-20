import {userStore} from '@/5-entities/user/stores/user-store';
import {Box, Stack} from '@mui/material';
import {AddUsersToEvent} from '@/4-features/AddUsersToEvent/ui/AddUsersToEvent';
import {observer} from 'mobx-react-lite';
import {Delete} from '@mui/icons-material';
import {userService} from '@/5-entities/user/services/user-service';

export const EventUsers = observer(() => {
  return (
    <Box padding={'16px'}>
      <Stack alignItems={'end'}>
        <AddUsersToEvent />
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
