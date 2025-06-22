import {User} from '@/5-entities/user/types/types';

export interface Event {
  id: string;
  name: string;
  currencyId: string;
  users: Array<User>;
  pinCode: string;
}

export type CreateEvent = Omit<Event, 'id' | 'users'> & {users: Array<Omit<User, 'id'>>};
