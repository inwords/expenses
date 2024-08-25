export type DateWithoutTime = string; //2024-12-31
export const getCurrentDateWithoutTime = (): DateWithoutTime => {
  const now = new Date();

  return getDateWithoutTimeWithMoscowTimezone(now);
};

const getDateWithoutTimeWithMoscowTimezone = (date: Date): DateWithoutTime => {
  const moscowLocale = date.toLocaleString('en-US', {timeZone: 'Europe/Moscow'});

  return new Date(moscowLocale).toLocaleDateString('sv-SE'); // yyyy-MM-dd
};
