export interface IEvent {
  id: string;
  name: string;
  currencyId: string;
  pinCode: string;
  deletedAt: Date | null;
  createdAt: Date;
  updatedAt: Date;
}
