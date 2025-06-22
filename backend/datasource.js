require('ts-node/register/transpile-only');

const dotenv = require('dotenv');
dotenv.config();

const {DataSource} = require('typeorm');
const {createTypeormConfigDefault, appDbConfig} = require('./src/frameworks/relational-data-service/postgres/config');

module.exports.dataSource = new DataSource(createTypeormConfigDefault(appDbConfig));
