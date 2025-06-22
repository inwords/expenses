import {GetAllCurrenciesUseCase} from './get-all-currencies.usecase';
import {GetEventInfoUseCase} from './get-event-info.usecase';
import {SaveEventUseCase} from './save-event.usecase';
import {GetEventExpensesUseCase} from './get-event-expenses.usecase';
import {SaveEventExpenseUseCase} from './save-event-expense.usecase';
import {SaveUsersToEventUseCase} from './save-users-to-event.usecase';

export const allUsersUseCases = [
  GetAllCurrenciesUseCase,
  GetEventInfoUseCase,
  SaveEventUseCase,
  GetEventExpensesUseCase,
  SaveEventExpenseUseCase,
  SaveUsersToEventUseCase,
];
