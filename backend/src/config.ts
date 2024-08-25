import * as process from 'process';

export const config = () => {
  return {
    POSTGRES_PORT: process.env.POSTGRES_PORT,
    POSTGRES_USER_NAME: process.env.POSTGRES_USER_NAME,
    POSTGRES_PASSWORD: process.env.POSTGRES_PASSWORD,
    POSTGRES_DATABASE: process.env.POSTGRES_DATABASE,
    POSTGRES_HOST: process.env.POSTGRES_HOST,
    OPEN_EXCHANGE_RATES_API_ID: process.env.OPEN_EXCHANGE_RATES_API_ID,
  } as const;
};
