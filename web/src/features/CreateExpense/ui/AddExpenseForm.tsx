import {FormContainer, TextFieldElement, useFieldArray, useForm} from 'react-hook-form-mui';
import {Button, Stack} from '@mui/material';
import {ExpenseDescriptionInput} from '@/features/AddExpense/ui/ExpenseNameInput';
import {ExpenseAmountInput} from '@/features/AddExpense/ui/ExpenseAmountInput';
import {SelectExpenseOwner} from '@/features/AddExpense/ui/SelectExpenseOwner';
import {SelectCurrency} from '@/features/AddExpense/ui/SelectCurrency';
import React from 'react';
import {SplitOptions} from '@/features/AddExpense/ui/SplitOption';
import {useParams} from 'react-router';
import {expenseService} from '@/entities/expense/services/expense-service';
import {observer} from 'mobx-react-lite';
import {userStore} from '@/entities/user/stores/user-store';
import {SelectUser} from '@/entities/user/ui/SelectUser';
import {CreateExpense} from '@/entities/expense/types/types';

export const AddExpenseForm = observer(() => {
  const {control} = useForm();
  const {fields, append} = useFieldArray({
    control, // control props comes from useForm (optional: if you are using FormProvider)
    name: 'users', // unique name for your Field Array
  });
  const {id} = useParams();
  const initialValues = {userWhoPaidId: userStore.currentUser?.id} as CreateExpense & {
    amount: number;
    splitOption: string;
  };

  return (
    <FormContainer
      onSuccess={(d) => {
        if (id) {
          const {amount, splitOption, ...rest} = d;

          expenseService.createExpense({
            ...rest,
            eventId: Number(id),
            splitInformation: d.splitInformation.map((i) => {
              return {...i, amount: Number(i.amount)};
            }),
          });
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

        {fields.map((field, index) => (
          <React.Fragment key={field.id}>
            <TextFieldElement name={`splitInformation.${index}.amount`} label={'Сумма к возврату'} required />

            <SelectUser label="Кто должен" name={`splitInformation.${index}.userId`} />
          </React.Fragment>
        ))}

        <Button onClick={() => append({})} variant={'outlined'}>
          Добавить персону
        </Button>
      </Stack>

      <Stack justifyContent="end" marginTop={'16px'}>
        <Button type={'submit'} variant="contained">
          Добавить трату
        </Button>
      </Stack>
    </FormContainer>
  );
});
