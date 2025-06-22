import {join} from 'path';
import {DataSourceOptions} from 'typeorm';
import {allEntities} from './entities';
import {env} from '../../../config';
import {PostgresNamingStrategy} from './postgres-naming-strategy';

interface DbConnectionStringConfig {
  host: string;
  port: string;
  dbname: string;
  user: string;
  password: string;
  target_session_attrs: string;
}

export interface DbConfig extends DbConnectionStringConfig {
  poolSize: number;
  connectionTimeoutMs: number;
  idleTimeoutMs: number;
  schema: string;
  logging: boolean;
}

export const appDbConfig: DbConfig = {
  host: env.POSTGRES_HOST,
  port: env.POSTGRES_PORT,
  dbname: env.POSTGRES_DATABASE,
  user: env.POSTGRES_USER_NAME,
  password: env.POSTGRES_PASSWORD,
  target_session_attrs: env.POSTGRES_MASTER_TARGET_SESSION_ATTRS,
  poolSize: env.POSTGRES_POOL_SIZE,
  connectionTimeoutMs: env.POSTGRES_POOL_CONNECTION_TIMEOUT_MS,
  idleTimeoutMs: env.POSTGRES_POOL_IDLE_TIMEOUT_MS,
  schema: env.POSTGRES_SCHEMA,
  logging: false,
};

export const createTypeormConfigDefault = (config: DbConfig): DataSourceOptions => {
  return {
    type: 'postgres',
    host: config.host,
    port: Number(config.port),
    database: config.dbname,
    username: config.user,
    password: config.password,
    entities: allEntities,
    migrations: [join(__dirname, '../../../../migrations/**/*.{ts,js}')],
    extra: {
      max: env.POSTGRES_POOL_SIZE,
      connectionTimeoutMillis: config.connectionTimeoutMs,
      idleTimeoutMillis: config.idleTimeoutMs,
    },
    namingStrategy: new PostgresNamingStrategy(),
    ...(config.schema === 'public' ? {} : {schema: config.schema}),
  };
};
