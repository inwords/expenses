import * as process from 'process';
import * as dotenv from 'dotenv';
dotenv.config();

export const config = () => {
  return {
    POSTGRES_PORT: process.env.POSTGRES_PORT,
    POSTGRES_USER_NAME: process.env.POSTGRES_USER_NAME,
    POSTGRES_PASSWORD: process.env.POSTGRES_PASSWORD,
    POSTGRES_DATABASE: process.env.POSTGRES_DATABASE,
    POSTGRES_HOST: process.env.POSTGRES_HOST,
    POSTGRES_SCHEMA: process.env.POSTGRES_SCHEMA,
    POSTGRES_POOL_SIZE: 5,
    POSTGRES_MASTER_TARGET_SESSION_ATTRS: 'read-write',
    POSTGRES_POOL_CONNECTION_TIMEOUT_MS: 10_000,
    POSTGRES_POOL_IDLE_TIMEOUT_MS: 10_000,
    OPEN_EXCHANGE_RATES_API_ID: process.env.OPEN_EXCHANGE_RATES_API_ID,
  } as const;
};

export const env = config();
