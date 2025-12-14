import {RelationalDataService} from '#frameworks/relational-data-service/postgres/relational-data-service';
import {appDbConfig} from '#frameworks/relational-data-service/postgres/config';
import {ExpenseType} from '#domain/entities/expense.entity';

describe('ExpenseRepository', () => {
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
    it('should insert expense correctly', async () => {
      const expense = {
        id: 'expense-1',
        description: 'Dinner',
        userWhoPaidId: 'user-1',
        currencyId: 'currency-1',
        eventId: 'event-1',
        expenseType: ExpenseType.Expense,
        splitInformation: [
          {
            userId: 'user-1',
            amount: 50,
            exchangedAmount: 50,
          },
          {
            userId: 'user-2',
            amount: 50,
            exchangedAmount: 50,
          },
        ],
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
      };

      const [result, queryDetails] = await relationalDataService.expense.insert(expense);

      // Объединяем результат и детали запроса для snapshot теста
      expect({result, queryDetails}).toMatchSnapshot();
    });
  });

  describe('findByEventId', () => {
    it('should find expenses by event id', async () => {
      // Сначала вставляем тестовые данные
      const expense = {
        id: 'expense-1',
        description: 'Dinner',
        userWhoPaidId: 'user-1',
        currencyId: 'currency-1',
        eventId: 'event-1',
        expenseType: ExpenseType.Expense,
        splitInformation: [
          {
            userId: 'user-1',
            amount: 50,
            exchangedAmount: 50,
          },
          {
            userId: 'user-2',
            amount: 50,
            exchangedAmount: 50,
          },
        ],
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
      };

      await relationalDataService.expense.insert(expense);

      // Теперь ищем расходы по event id
      const [result, queryDetails] = await relationalDataService.expense.findByEventId('event-1');

      // Объединяем результат и детали запроса для snapshot теста
      expect({result, queryDetails}).toMatchSnapshot();
    });

    it('should return empty array for non-existent event', async () => {
      const [result, queryDetails] = await relationalDataService.expense.findByEventId('non-existent');

      // Объединяем результат и детали запроса для snapshot теста
      expect({result, queryDetails}).toMatchSnapshot();
    });
  });

  describe('deleteByEventId', () => {
    it('should delete all expenses for event', async () => {
      // Вставляем несколько расходов для одного события
      const expense1 = {
        id: 'expense-1',
        description: 'Dinner',
        userWhoPaidId: 'user-1',
        currencyId: 'currency-1',
        eventId: 'event-1',
        expenseType: ExpenseType.Expense,
        splitInformation: [{userId: 'user-1', amount: 50, exchangedAmount: 50}],
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
      };

      const expense2 = {
        id: 'expense-2',
        description: 'Lunch',
        userWhoPaidId: 'user-2',
        currencyId: 'currency-1',
        eventId: 'event-1',
        expenseType: ExpenseType.Expense,
        splitInformation: [{userId: 'user-2', amount: 30, exchangedAmount: 30}],
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
      };

      await relationalDataService.expense.insert(expense1);
      await relationalDataService.expense.insert(expense2);

      // Удаляем все расходы для события
      const [result, queryDetails] = await relationalDataService.expense.deleteByEventId('event-1');

      expect({result, queryDetails}).toMatchSnapshot();

      // Проверяем, что расходы удалены
      const [expenses] = await relationalDataService.expense.findByEventId('event-1');
      expect(expenses).toHaveLength(0);
    });

    it('should not affect expenses from other events', async () => {
      // Вставляем расходы для двух разных событий
      const expense1 = {
        id: 'expense-1',
        description: 'Dinner',
        userWhoPaidId: 'user-1',
        currencyId: 'currency-1',
        eventId: 'event-1',
        expenseType: ExpenseType.Expense,
        splitInformation: [{userId: 'user-1', amount: 50, exchangedAmount: 50}],
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
      };

      const expense2 = {
        id: 'expense-2',
        description: 'Lunch',
        userWhoPaidId: 'user-2',
        currencyId: 'currency-1',
        eventId: 'event-2',
        expenseType: ExpenseType.Expense,
        splitInformation: [{userId: 'user-2', amount: 30, exchangedAmount: 30}],
        createdAt: new Date('2023-01-01T00:00:00Z'),
        updatedAt: new Date('2023-01-01T00:00:00Z'),
      };

      await relationalDataService.expense.insert(expense1);
      await relationalDataService.expense.insert(expense2);

      // Удаляем расходы только для event-1
      await relationalDataService.expense.deleteByEventId('event-1');

      // Проверяем, что расходы event-2 остались
      const [expenses] = await relationalDataService.expense.findByEventId('event-2');
      expect(expenses).toHaveLength(1);
    });
  });
});
