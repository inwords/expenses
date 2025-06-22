export type DateIsoString = string; // 2021-01-01T00:00:00.000Z
export type PartialByKeys<T, K extends keyof T> = Omit<T, K> & Partial<Pick<T, K>>;
