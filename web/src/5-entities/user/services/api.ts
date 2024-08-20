import {httpClient} from '@/6-shared/api/http-client';
import {User} from '@/5-entities/user/types/types';

export const addUsersToEvent = async (eventId: string, users: Omit<User, 'id'>, pinCode: string) => {
  try {
    return await httpClient.request(`/event/${eventId}/users`, {
      method: 'POST',
      body: JSON.stringify({
        users,
        pinCode,
      }),
    });
  } catch (error) {}
};

export const deleteUserFromEvent = async (eventId: string, userId: number, pinCode: string) => {
  try {
    return await httpClient.request(
      `/event${buildQueryString({
        eventId,
        pinCode,
        userId: String(userId),
      })}`,
      {
        method: 'DELETE',
      },
    );
  } catch (error) {}
};

const buildQueryString = (obj: Record<string, string>) => {
  const entries = Object.entries(obj);

  return entries.reduce((prev, [key, val], i) => {
    prev += `${key}=${val}${i === entries.length - 1 ? '' : '&'}`;

    return prev;
  }, '?');
};
