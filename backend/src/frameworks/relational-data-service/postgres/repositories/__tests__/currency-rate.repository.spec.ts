import {RelationalDataService} from '#frameworks/relational-data-service/postgres/relational-data-service';
import {appDbConfig} from '#frameworks/relational-data-service/postgres/config';

describe('CurrencyRateRepository', () => {
  let relationalDataService: RelationalDataService;

  beforeAll(async () => {
    relationalDataService = new RelationalDataService({
      dbConfig: appDbConfig,
      showQueryDetails: true,
    });
    await relationalDataService.initialize();
    // Очищаем базу данных перед запуском тестов
    await relationalDataService.flush();
  });

  afterAll(async () => {
    await relationalDataService.destroy();
  });

  beforeEach(async () => {
    await relationalDataService.flush();
  });

  describe('insert', () => {
    it('should insert currency rate correctly', async () => {
      const currencyRate = {
        date: '2023-01-01',
        rate: {
          EUR: 1,
          USD: 1.05,
          RUB: 80.5,
        },
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
      };

      const [result, queryDetails] = await relationalDataService.currencyRate.insert(currencyRate);

      // Объединяем результат и детали запроса для snapshot теста
      expect({result, queryDetails}).toMatchSnapshot();
    });
  });

  describe('findByDate', () => {
    it('should find currency rate by date', async () => {
      // Сначала вставляем тестовые данные
      const currencyRate = {
        date: '2023-01-01',
        rate: {
          EUR: 1,
          USD: 1.05,
          RUB: 80.5,
        },
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
      };

      await relationalDataService.currencyRate.insert(currencyRate);

      // Теперь ищем эти данные
      const [result, queryDetails] = await relationalDataService.currencyRate.findByDate('2023-01-01');

      // Объединяем результат и детали запроса для snapshot теста
      expect({result, queryDetails}).toMatchSnapshot();
    });

    it('should return null for non-existent date', async () => {
      const [result, queryDetails] = await relationalDataService.currencyRate.findByDate('2023-12-31');

      // Объединяем результат и детали запроса для snapshot теста
      expect({result, queryDetails}).toMatchSnapshot();
    });
  });
});
