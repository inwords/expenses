import {SelectElement} from 'react-hook-form-mui';
import {observer} from 'mobx-react-lite';
import {userStore} from '@/entities/user/stores/user-store';
interface Props {
  name: string;
  label: string;
}

export const SelectUser = observer(({name, label}: Props) => {
  return <SelectElement name={name} label={label} options={userStore.usersToSelect} />;
});
