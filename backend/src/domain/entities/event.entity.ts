export interface IEvent {
  id: string;
  name: string;
  currencyId: string;
  pinCode: string;
  createdAt: Date;
  updatedAt: Date;
  deletedAt: Date | null;
}
