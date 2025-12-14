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

    it('should not return soft-deleted event', async () => {
      // Вставляем событие
      const event = {
        id: 'event-1',
        name: 'Test Event',
        currencyId: 'currency-1',
        pinCode: '1234',
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
      };

      await relationalDataService.event.insert(event);

      // Мягко удаляем событие
      await relationalDataService.event.softDeleteById('event-1', new Date('2023-06-01T00:00:00Z'));

      // findById не должен вернуть удаленное событие
      const [result, queryDetails] = await relationalDataService.event.findById('event-1');

      expect({result, queryDetails}).toMatchSnapshot();
    });
  });

  describe('findByIdIncludingDeleted', () => {
    it('should find event by id including deleted', async () => {
      // Вставляем событие
      const event = {
        id: 'event-1',
        name: 'Test Event',
        currencyId: 'currency-1',
        pinCode: '1234',
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
      };

      await relationalDataService.event.insert(event);

      // Ищем события (не удаленное)
      const [result, queryDetails] = await relationalDataService.event.findByIdIncludingDeleted('event-1');

      expect({result, queryDetails}).toMatchSnapshot();
    });

    it('should find soft-deleted event', async () => {
      // Вставляем событие
      const event = {
        id: 'event-1',
        name: 'Test Event',
        currencyId: 'currency-1',
        pinCode: '1234',
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
      };

      await relationalDataService.event.insert(event);

      // Мягко удаляем событие
      await relationalDataService.event.softDeleteById('event-1', new Date('2023-06-01T00:00:00Z'));

      // findByIdIncludingDeleted должен вернуть удаленное событие
      const [result, queryDetails] = await relationalDataService.event.findByIdIncludingDeleted('event-1');

      expect({result, queryDetails}).toMatchSnapshot();
    });

    it('should return null for non-existent event', async () => {
      const [result, queryDetails] = await relationalDataService.event.findByIdIncludingDeleted('non-existent');

      expect({result, queryDetails}).toMatchSnapshot();
    });
  });

  describe('softDeleteById', () => {
    it('should soft delete event by setting deletedAt', async () => {
      // Вставляем событие
      const event = {
        id: 'event-1',
        name: 'Test Event',
        currencyId: 'currency-1',
        pinCode: '1234',
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
      };

      await relationalDataService.event.insert(event);

      // Мягко удаляем событие
      const deletedAt = new Date('2023-06-01T00:00:00Z');
      const [result, queryDetails] = await relationalDataService.event.softDeleteById('event-1', deletedAt);

      expect({result, queryDetails}).toMatchSnapshot();
    });
  });
});
