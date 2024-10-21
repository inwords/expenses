export const UserRoutes = {
  root: '/user',
  getAllCurrencies: `/currencies/all`,
  createEvent: '/event',
  getEventInfo: `/event/:eventId`,
  addUsersToEvent: `/event/:eventId/users`,
  getAllEventExpenses: `/event/:eventId/expenses`,
  createExpense: `/:eventId/expense`,
};
