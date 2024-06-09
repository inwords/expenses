require('ts-node/register/transpile-only');

const dotenv = require('dotenv');
dotenv.config();

const {DataSource} = require('typeorm');
const {config} = require('./src/config');
const {join} = require('path');
const {SnakeNamingStrategy} = require('typeorm-naming-strategies');

const configuration = config();

module.exports.dataSource = new DataSource({
  type: 'postgres',
  host: configuration.POSTGRES_HOST,
  port: configuration.POSTGRES_PORT,
  username: configuration.POSTGRES_USER_NAME,
  password: configuration.POSTGRES_PASSWORD,
  database: configuration.POSTGRES_DATABASE,
  entities: [join(__dirname, `src/**/*.entity.{ts,js}`)],
  migrations: [join(__dirname, `migrations/default/**/*.{ts,js}`)],
  namingStrategy: new SnakeNamingStrategy(),
});
