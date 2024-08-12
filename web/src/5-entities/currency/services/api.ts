import {httpClient} from '@/6-shared/api/http-client';

export const getCurrencies = async () => {
  return await httpClient.request('/currency/all', {method: 'GET'});
};
