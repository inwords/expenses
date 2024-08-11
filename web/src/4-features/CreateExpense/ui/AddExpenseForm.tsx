import {FormContainer, TextFieldElement, useFieldArray, useForm} from 'react-hook-form-mui';
import {Button, Stack} from '@mui/material';
import {ExpenseDescriptionInput} from '@/4-features/AddExpense/ui/ExpenseNameInput';
import {ExpenseAmountInput} from '@/4-features/AddExpense/ui/ExpenseAmountInput';
import {SelectExpenseOwner} from '@/4-features/AddExpense/ui/SelectExpenseOwner';
import {SelectCurrency} from '@/4-features/AddExpense/ui/SelectCurrency';
import React from 'react';
import {SplitOptions} from '@/4-features/AddExpense/ui/SplitOption';
import {useParams} from 'react-router';
import {expenseService} from '@/5-entities/expense/services/expense-service';
import {observer} from 'mobx-react-lite';
import {userStore} from '@/5-entities/user/stores/user-store';
import {SelectUser} from '@/5-entities/user/ui/SelectUser';
import {CreateExpense} from '@/5-entities/expense/types/types';
import {expenseStore} from '@/5-entities/expense/stores/expense-store';

interface Props {
  onSuccess?: (isModalOpen: boolean) => void;
}

export const AddExpenseForm = observer(({onSuccess}: Props) => {
  const {control} = useForm();
  const {fields, append} = useFieldArray({
    control, // control props comes from useForm (optional: if you are using FormProvider)
    name: 'users', // unique name for your Field Array
  });
  const {id} = useParams();
  const initialValues = {userWhoPaidId: userStore.currentUser?.id, splitOption: '1'} as CreateExpense & {
    amount: number;
    splitOption: string;
  };

  const isSplitEqually = expenseStore.splitOption === '1';

  return (
    <FormContainer
      onSuccess={async (d) => {
        if (id) {
          const {amount, splitOption, ...rest} = d;

          await expenseService.createExpense({
            ...rest,
            eventId: Number(id),
            splitInformation: isSplitEqually
              ? userStore.users.map((u) => {
                  return {userId: String(u.id), amount: Math.round(Number(d.amount) / userStore.users.length)};
                })
              : d.splitInformation.map((i) => {
                  return {...i, amount: Number(i.amount)};
                }),
          });

          onSuccess?.(false);
        }
      }}
      defaultValues={initialValues}
    >
      <Stack spacing={2} maxWidth={600}>
        <ExpenseDescriptionInput />

        <ExpenseAmountInput />

        <SelectExpenseOwner />

        <SelectCurrency />

        <SplitOptions />

        {!isSplitEqually && (
          <>
            {fields.map((field, index) => (
              <React.Fragment key={field.id}>
                <TextFieldElement name={`splitInformation.${index}.amount`} label={'Сумма к возврату'} required />

                <SelectUser label="Кто должен" name={`splitInformation.${index}.userId`} />
              </React.Fragment>
            ))}

            <Button onClick={() => append({})} variant={'outlined'}>
              Добавить персону
            </Button>
          </>
        )}
      </Stack>

      <Stack justifyContent="end" marginTop={'16px'}>
        <Button type={'submit'} variant="contained">
          Добавить трату
        </Button>
      </Stack>
    </FormContainer>
  );
});