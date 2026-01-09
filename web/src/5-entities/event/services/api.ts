import {CreateEvent} from '@/5-entities/event/types/types';
import {httpClient} from '@/6-shared/api/http-client';

export const createEvent = async (event: CreateEvent) => {
  try {
    return await httpClient.request('/user/event', {
      method: 'POST',
      body: JSON.stringify(event),
    });
  } catch (error) {}
};

export const getEventInfo = async (eventId: string, params: {pinCode?: string; token?: string}) => {
  try {
    const fetchUrl = `/v2/user/event/${eventId}`;

    return await httpClient.request(fetchUrl, {
      method: 'POST',
      body: JSON.stringify(params),
    });
  } catch (error) {
    console.error('An error occurred:', error);
  }
};
