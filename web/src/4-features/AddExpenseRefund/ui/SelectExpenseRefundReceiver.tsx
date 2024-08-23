import {SelectUser} from '@/5-entities/user/ui/SelectUser';

export const SelectExpenseRefundReceiver = () => {
  return <SelectUser label="Кто получает" name="userWhoReceiveId" />;
};
