import {httpClient} from '@/6-shared/api/http-client';

export const getCurrencies = async () => {
  return await httpClient.request('/user/currencies/all', {method: 'GET'});
};
