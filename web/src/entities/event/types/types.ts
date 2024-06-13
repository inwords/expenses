import {User} from '@/entities/user/types/types';

export interface Event {
  id: string;
  name: string;
  ownerId: string;
  currencyId: string;
  users: Array<User>;
  pinCode: string;
}

export type CreateEvent = Omit<Event, 'id' | 'users'> & {users: Array<Omit<User, 'id'>>};
