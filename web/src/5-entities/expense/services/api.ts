import {CreateExpense} from '@/5-entities/expense/types/types';
import {httpClient} from '@/6-shared/api/http-client';

export const getEventExpenses = async (eventId: string) => {
  try {
    const fetchUrl = `/expense/${eventId}/expenses`;

    return await httpClient.request(fetchUrl, {
      method: 'GET',
    });
  } catch (error) {
    console.error('An error occurred:', error);
  }
};

export const createExpense = async (expense: CreateExpense) => {
  try {
    return await httpClient.request('/expense', {
      method: 'POST',
      body: JSON.stringify(expense),
    });
  } catch (error) {}
};
