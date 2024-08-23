export const EXPENSE_ROUTES = {
  root: '/expense',
  getAllEventExpenses: `/:eventId/expenses`,
  createExpense: `/`,
};

export enum ExpenseType {
  Expense = 'expense',
  Refund = 'refund',
}
