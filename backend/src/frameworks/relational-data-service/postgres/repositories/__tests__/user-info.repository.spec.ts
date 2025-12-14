import {RelationalDataService} from '#frameworks/relational-data-service/postgres/relational-data-service';
import {appDbConfig} from '#frameworks/relational-data-service/postgres/config';

describe('UserInfoRepository', () => {
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
    it('should insert single user info correctly', async () => {
      const userInfo = {
        id: 'user-info-1',
        name: 'John Doe',
        eventId: 'event-1',
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
      };

      const [result, queryDetails] = await relationalDataService.userInfo.insert(userInfo);

      // Объединяем результат и детали запроса для snapshot теста
      expect({result, queryDetails}).toMatchSnapshot();
    });

    it('should insert multiple user infos correctly', async () => {
      const userInfos = [
        {
          id: 'user-info-1',
          name: 'John Doe',
          eventId: 'event-1',
          createdAt: new Date('2023-01-01T00:00:00Z'),
          updatedAt: new Date('2023-01-01T00:00:00Z'),
        },
        {
          id: 'user-info-2',
          name: 'Jane Smith',
          eventId: 'event-1',
          createdAt: new Date('2023-01-01T00:00:00Z'),
          updatedAt: new Date('2023-01-01T00:00:00Z'),
        },
      ];

      const [result, queryDetails] = await relationalDataService.userInfo.insert(userInfos);

      // Объединяем результат и детали запроса для snapshot теста
      expect({result, queryDetails}).toMatchSnapshot();
    });
  });

  describe('findByEventId', () => {
    it('should find user infos by event id', async () => {
      // Сначала вставляем тестовые данные
      const userInfos = [
        {
          id: 'user-info-1',
          name: 'John Doe',
          eventId: 'event-1',
          createdAt: new Date('2023-01-01T00:00:00Z'),
          updatedAt: new Date('2023-01-01T00:00:00Z'),
        },
        {
          id: 'user-info-2',
          name: 'Jane Smith',
          eventId: 'event-1',
          createdAt: new Date('2023-01-01T00:00:00Z'),
          updatedAt: new Date('2023-01-01T00:00:00Z'),
        },
      ];

      await relationalDataService.userInfo.insert(userInfos);

      // Теперь ищем информацию о пользователях по event id
      const [result, queryDetails] = await relationalDataService.userInfo.findByEventId('event-1');

      // Объединяем результат и детали запроса для snapshot теста
      expect({result, queryDetails}).toMatchSnapshot();
    });

    it('should return empty array for non-existent event', async () => {
      const [result, queryDetails] = await relationalDataService.userInfo.findByEventId('non-existent');

      // Объединяем результат и детали запроса для snapshot теста
      expect({result, queryDetails}).toMatchSnapshot();
    });
  });

  describe('deleteByEventId', () => {
    it('should delete all user infos for event', async () => {
      // Вставляем пользователей для события
      const userInfos = [
        {
          id: 'user-info-1',
          name: 'John Doe',
          eventId: 'event-1',
          createdAt: new Date('2023-01-01T00:00:00Z'),
          updatedAt: new Date('2023-01-01T00:00:00Z'),
        },
        {
          id: 'user-info-2',
          name: 'Jane Smith',
          eventId: 'event-1',
          createdAt: new Date('2023-01-01T00:00:00Z'),
          updatedAt: new Date('2023-01-01T00:00:00Z'),
        },
      ];

      await relationalDataService.userInfo.insert(userInfos);

      // Удаляем всех пользователей для события
      const [result, queryDetails] = await relationalDataService.userInfo.deleteByEventId('event-1');

      expect({result, queryDetails}).toMatchSnapshot();

      // Проверяем, что пользователи удалены
      const [users] = await relationalDataService.userInfo.findByEventId('event-1');
      expect(users).toHaveLength(0);
    });

    it('should not affect user infos from other events', async () => {
      // Вставляем пользователей для двух разных событий
      const userInfo1 = {
        id: 'user-info-1',
        name: 'John Doe',
        eventId: 'event-1',
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
      };

      const userInfo2 = {
        id: 'user-info-2',
        name: 'Jane Smith',
        eventId: 'event-2',
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
      };

      await relationalDataService.userInfo.insert(userInfo1);
      await relationalDataService.userInfo.insert(userInfo2);

      // Удаляем пользователей только для event-1
      await relationalDataService.userInfo.deleteByEventId('event-1');

      // Проверяем, что пользователи event-2 остались
      const [users] = await relationalDataService.userInfo.findByEventId('event-2');
      expect(users).toHaveLength(1);
    });
  });
});
