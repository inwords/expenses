import {SelectUser} from '@/entities/user/ui/SelectUser';

export const SelectExpenseOwner = () => {
  return <SelectUser label="Кто оплачивал" name="owner" />;
};
