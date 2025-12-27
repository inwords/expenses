export type DateWithoutTime = string; //2024-12-31

/**
 * Get date in UTC timezone in YYYY-MM-DD format
 * @param date - Date to convert to UTC
 * @returns Date in UTC as string in format YYYY-MM-DD
 */
export const getDateWithoutTimeUTC = (date: Date): DateWithoutTime => {
  const year = date.getUTCFullYear();
  const month = String(date.getUTCMonth() + 1).padStart(2, '0');
  const day = String(date.getUTCDate()).padStart(2, '0');

  return `${year}-${month}-${day}`; // yyyy-MM-dd
};

/**
 * Get current date in UTC timezone in YYYY-MM-DD format
 * Used for fetching currency rates from Open Exchange Rates API which operates in UTC
 * @returns Current date in UTC as string in format YYYY-MM-DD
 */
export const getCurrentDateWithoutTimeUTC = (): DateWithoutTime => {
  return getDateWithoutTimeUTC(new Date());
};
