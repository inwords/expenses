/* eslint-disable import/order */
/* eslint-disable import/first */

require('tsconfig-paths/register');

import * as dotenv from 'dotenv';
dotenv.config();

import {RelationalDataService} from '#frameworks/relational-data-service/postgres/relational-data-service';
import {appDbConfig} from '#frameworks/relational-data-service/postgres/config';

void (async () => {
  const dataService = new RelationalDataService({
    showQueryDetails: false,
    dbConfig: {
      ...appDbConfig,
      logging: true,
    },
  });

  await dataService.initialize();

  await dataService.dataSource.runMigrations({
    transaction: 'each',
  });
  await dataService.destroy();
})();
