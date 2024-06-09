import {CreateEvent} from '@/entities/event/types/types';

export const createEvent = async (event: CreateEvent) => {
  try {
    const response = await fetch('http://localhost:3001/event', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(event),
    });

    const responseData = await response.json();
    return responseData;
  } catch (error) {}
};

export const getEventInfo = async (eventId: string, queryParams: Record<string, string>) => {
  try {
    const query = new URLSearchParams(queryParams).toString();
    const fetchUrl = `http://localhost:3001/event/${eventId}?${query}`;

    const response = await fetch(fetchUrl, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    const responseData = await response.json();
    return responseData;
  } catch (error) {
    console.error('An error occurred:', error);
  }
};
