import {CreateExpense} from '@/5-entities/expense/types/types';
import {httpClient} from '@/6-shared/api/http-client';

export const getEventExpenses = async (eventId: string) => {
  try {
    const fetchUrl = `/user/event/${eventId}/expenses`;

    return await httpClient.request(fetchUrl, {
      method: 'GET',
    });
  } catch (error) {
    console.error('An error occurred:', error);
  }
};

export const createExpense = async (expense: CreateExpense) => {
  const {eventId, ...rest} = expense;

  try {
    return await httpClient.request(`/user/event/${eventId}/expense`, {
      method: 'POST',
      body: JSON.stringify(rest),
    });
  } catch (error) {}
};
