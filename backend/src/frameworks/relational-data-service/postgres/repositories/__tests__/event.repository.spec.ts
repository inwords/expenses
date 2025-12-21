import {RelationalDataService} from '#frameworks/relational-data-service/postgres/relational-data-service';
import {appDbConfig} from '#frameworks/relational-data-service/postgres/config';
import {IEvent} from '#domain/entities/event.entity';
import {IQueryDetails} from '#domain/abstracts/relational-data-service/types';

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
        deletedAt: null,
      };

      const [, queryDetails] = await relationalDataService.event.insert(event);
      const [insertedEvent] = await relationalDataService.event.findById('event-1');

      expect(insertedEvent).toMatchObject(event);
      expect(queryDetails).toMatchSnapshot();
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
        deletedAt: null,
      };

      await relationalDataService.event.insert(event);

      // Теперь ищем эти данные
      const [result, queryDetails] = await relationalDataService.event.findById('event-1');

      expect(result).toMatchObject(event);
      expect(queryDetails).toMatchSnapshot();
    });

    it('should return null for non-existent event', async () => {
      const [result, queryDetails] = await relationalDataService.event.findById('non-existent');

      expect(result).toBeNull();
      expect(queryDetails).toMatchSnapshot();
    });

    it('should find event by id with pessimistic_write lock', async () => {
      // Сначала вставляем тестовые данные
      const event = {
        id: 'event-1',
        name: 'Test Event',
        currencyId: 'currency-1',
        pinCode: '1234',
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
        deletedAt: null,
      };

      await relationalDataService.event.insert(event);

      // Ищем с блокировкой в транзакции
      const result: {result: IEvent | null; queryDetails: IQueryDetails} = await relationalDataService.transaction(
        async (ctx) => {
          const [foundEvent, queryDetails] = await relationalDataService.event.findById('event-1', {
            ctx,
            lock: 'pessimistic_write',
          });

          return {result: foundEvent, queryDetails};
        },
      );

      expect(result.result).toMatchObject(event);
      expect(result.queryDetails).toMatchSnapshot();
    });
  });

  describe('update', () => {
    it('should update event correctly', async () => {
      // Сначала вставляем тестовые данные
      const event = {
        id: 'event-1',
        name: 'Test Event',
        currencyId: 'currency-1',
        pinCode: '1234',
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
        deletedAt: null,
      };

      await relationalDataService.event.insert(event);

      // Обновляем событие
      const newDeletedAt = new Date('2023-01-02T00:00:00Z');
      const newUpdatedAt = new Date('2023-01-02T00:00:00Z');

      const [, queryDetails] = await relationalDataService.event.update('event-1', {
        deletedAt: newDeletedAt,
        updatedAt: newUpdatedAt,
      });

      // Проверяем что данные действительно обновились
      const [updatedEvent] = await relationalDataService.event.findById('event-1');

      expect(updatedEvent).toMatchObject({
        ...event,
        deletedAt: newDeletedAt,
        updatedAt: newUpdatedAt,
      });
      expect(queryDetails).toMatchSnapshot();
    });

    it('should work within transaction', async () => {
      // Сначала вставляем тестовые данные
      const event = {
        id: 'event-1',
        name: 'Test Event',
        currencyId: 'currency-1',
        pinCode: '1234',
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
        deletedAt: null,
      };

      await relationalDataService.event.insert(event);

      const newDeletedAt = new Date('2023-01-02T00:00:00Z');
      const newUpdatedAt = new Date('2023-01-02T00:00:00Z');

      // Обновляем в транзакции
      await relationalDataService.transaction(async (ctx) => {
        const [, queryDetails] = await relationalDataService.event.update(
          'event-1',
          {
            deletedAt: newDeletedAt,
            updatedAt: newUpdatedAt,
          },
          {ctx},
        );

        expect(queryDetails.queryString).toContain('UPDATE');
      });

      // Проверяем что изменения применились
      const [updatedEvent] = await relationalDataService.event.findById('event-1');
      expect(updatedEvent).toMatchObject({
        ...event,
        deletedAt: newDeletedAt,
        updatedAt: newUpdatedAt,
      });
    });
  });
});
