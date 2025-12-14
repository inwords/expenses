import {RelationalDataService} from '#frameworks/relational-data-service/postgres/relational-data-service';
import {appDbConfig} from '#frameworks/relational-data-service/postgres/config';

describe('EventRepository', () => {
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
    it('should insert event correctly', async () => {
      const event = {
        id: 'event-1',
        name: 'Test Event',
        currencyId: 'currency-1',
        pinCode: '1234',
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
      };

      const [result, queryDetails] = await relationalDataService.event.insert(event);

      // Объединяем результат и детали запроса для snapshot теста
      expect({result, queryDetails}).toMatchSnapshot();
    });
  });

  describe('findById', () => {
    it('should find event by id', async () => {
      // Сначала вставляем тестовые данные
      const event = {
        id: 'event-1',
        name: 'Test Event',
        currencyId: 'currency-1',
        pinCode: '1234',
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
      };

      await relationalDataService.event.insert(event);

      // Теперь ищем эти данные
      const [result, queryDetails] = await relationalDataService.event.findById('event-1');

      // Объединяем результат и детали запроса для snapshot теста
      expect({result, queryDetails}).toMatchSnapshot();
    });

    it('should return null for non-existent event', async () => {
      const [result, queryDetails] = await relationalDataService.event.findById('non-existent');

      // Объединяем результат и детали запроса для snapshot теста
      expect({result, queryDetails}).toMatchSnapshot();
    });
  });
});
