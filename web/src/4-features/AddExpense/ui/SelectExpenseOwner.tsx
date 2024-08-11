import {SelectUser} from '@/5-entities/user/ui/SelectUser';

export const SelectExpenseOwner = () => {
  return <SelectUser label="Кто оплачивал" name="userWhoPaidId" />;
};
