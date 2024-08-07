import {UserAvatar} from '@/features/SelectUser/ui/UserAvatar';
import {Stack} from '@mui/material';
import {observer} from 'mobx-react-lite';
import {userStore} from '@/entities/user/stores/user-store';

export const SelectUserList = observer(() => {
  return (
    <Stack justifyContent={userStore.currentUser && 'end'} direction="row">
      {userStore.users.map((u) => {
        if (!userStore.currentUser) {
          return (
            <UserAvatar
              key={u.id}
              letter={u.name[0]}
              isSelected={false}
              onClick={() => userStore.setCurrentUser(u)}
            />
          );
        } else {
          return (
            u.id === userStore.currentUser.id && (
              <UserAvatar
                key={u.id}
                letter={u.name[0]}
                isSelected={userStore.currentUser.id === u.id}
                onClick={() => userStore.setCurrentUser(u)}
              />
            )
          );
        }
      })}
    </Stack>
  );
});
