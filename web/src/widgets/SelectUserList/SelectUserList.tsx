import {useState} from 'react';
import {UserAvatar} from '@/features/SelectUser/ui/UserAvatar';
import {Stack} from '@mui/material';

const USERS = [
  {name: 'Ivan', id: '1'},
  {name: 'Artem', id: '2'},
  {name: 'Vasya', id: '3'},
];
export const SelectUserList = () => {
  const [user, setUser] = useState<string>();

  return (
    <Stack justifyContent={user && 'end'} direction="row">
      {USERS.map((u) => {
        if (!user) {
          return <UserAvatar key={u.id} letter={u.name[0]} isSelected={user === u.id} onClick={() => setUser(u.id)} />;
        } else {
          return (
            u.id === user && (
              <UserAvatar key={u.id} letter={u.name[0]} isSelected={user === u.id} onClick={() => setUser(u.id)} />
            )
          );
        }
      })}
    </Stack>
  );
};
