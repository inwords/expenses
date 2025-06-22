export interface ICurrencyRate {
  date: string;
  rate: Record<string, number>;
  createdAt: Date;
  updatedAt: Date;
}
