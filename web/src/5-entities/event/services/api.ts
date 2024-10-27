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

export const getEventInfo = async (eventId: string, queryParams: Record<string, string>) => {
  try {
    const query = new URLSearchParams(queryParams).toString();
    const fetchUrl = `/user/event/${eventId}?${query}`;

    return await httpClient.request(fetchUrl, {
      method: 'GET',
    });
  } catch (error) {
    console.error('An error occurred:', error);
  }
};
