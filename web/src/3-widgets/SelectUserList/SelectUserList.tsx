import {UserAvatar} from '@/4-features/SelectUser/ui/UserAvatar';
import {Box, Stack} from '@mui/material';
import {observer} from 'mobx-react-lite';
import {userStore} from '@/5-entities/user/stores/user-store';
import {AddUsersToEvent} from '@/4-features/AddUsersToEvent/ui/AddUsersToEvent';

export const SelectUserList = observer(() => {
  if (userStore.currentUser) {
    return null;
  }

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh">
      <Stack direction={'column'} alignItems={'center'} spacing={2}>
        <h1>Выберите участника</h1>

        <Stack justifyContent={userStore.currentUser && 'end'} direction="row">
          {userStore.users.map((u) => {
            return (
              <UserAvatar
                key={u.id}
                letter={u.name[0]}
                isSelected={false}
                onClick={() => userStore.setCurrentUser(u)}
              />
            );
          })}
        </Stack>

        <AddUsersToEvent />
      </Stack>
    </Box>
  );
});
