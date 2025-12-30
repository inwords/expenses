export const UserRoutes = {
  root: '/user',
  getAllCurrencies: `/currencies/all`,
  createEvent: '/event',
  getEventInfo: `/event/:eventId`,
  deleteEvent: `/event/:eventId`,
  addUsersToEvent: `/event/:eventId/users`,
  getAllEventExpenses: `/event/:eventId/expenses`,
  createExpense: `/event/:eventId/expense`,
};

export const UserV2Routes = {
  root: '/v2/user',
  getEventInfo: `/event/:eventId/info`,
  addUsersToEvent: `/event/:eventId/users`,
  getAllEventExpenses: `/event/:eventId/expenses`,
  createExpense: `/event/:eventId/expense`,
};
