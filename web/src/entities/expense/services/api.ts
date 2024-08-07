import {CreateExpense} from '@/entities/expense/types/types';

export const getEventExpenses = async (eventId: string) => {
  try {
    const fetchUrl = `/expense/${eventId}/expenses`;

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

export const createExpense = async (expense: CreateExpense) => {
  try {
    const response = await fetch('/expense', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(expense),
    });

    const responseData = await response.json();
    return responseData;
  } catch (error) {}
};
