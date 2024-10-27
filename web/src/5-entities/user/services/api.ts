import {httpClient} from '@/6-shared/api/http-client';
import {User} from '@/5-entities/user/types/types';

export const addUsersToEvent = async (eventId: string, users: Omit<User, 'id'>, pinCode: string) => {
  try {
    return await httpClient.request(`/user/event/${eventId}/users`, {
      method: 'POST',
      body: JSON.stringify({
        users,
        pinCode,
      }),
    });
  } catch (error) {}
};
