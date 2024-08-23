import {SelectUser} from '@/5-entities/user/ui/SelectUser';

export const SelectExpenseRefundOwner = () => {
  return <SelectUser label="Кто возвращает" name="userWhoPaidId" />;
};
