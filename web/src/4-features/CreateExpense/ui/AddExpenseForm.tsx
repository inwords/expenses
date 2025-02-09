import {FormContainer, TextFieldElement, useFieldArray, useForm} from 'react-hook-form-mui';
import {Button, Stack} from '@mui/material';
import {ExpenseDescriptionInput} from '@/4-features/AddExpense/ui/ExpenseNameInput';
import {ExpenseAmountInput} from '@/4-features/AddExpense/ui/ExpenseAmountInput';
import {SelectExpenseOwner} from '@/4-features/AddExpense/ui/SelectExpenseOwner';
import React, {useEffect} from 'react';
import {SplitOptions} from '@/4-features/AddExpense/ui/SplitOption';
import {useParams} from 'react-router';
import {observer} from 'mobx-react-lite';
import {userStore} from '@/5-entities/user/stores/user-store';
import {SelectUser} from '@/5-entities/user/ui/SelectUser';
import {CreateExpenseForm} from '@/5-entities/expense/types/types';
import {expenseStore} from '@/5-entities/expense/stores/expense-store';
import {SelectCurrency} from '@/5-entities/currency/ui/SelectCurrency';

interface Props {
  onSuccess?: (isModalOpen: boolean, data: CreateExpenseForm, id: string) => void;
}

export const AddExpenseForm = observer(({onSuccess}: Props) => {
  const {control} = useForm();
  const {fields, append} = useFieldArray({
    control, // control props comes from useForm (optional: if you are using FormProvider)
    name: 'users', // unique name for your Field Array
  });
  const {id} = useParams();
  const initialValues = {userWhoPaidId: userStore.currentUser?.id, splitOption: '1'} as CreateExpenseForm;

  const isSplitEqually = expenseStore.splitOption === '1';

  useEffect(() => {
    if (!isSplitEqually) {
      append({});
    }
  }, [expenseStore.splitOption]);

  return (
    <FormContainer
      onSuccess={async (d) => {
        if (id) {
          onSuccess?.(false, d, id);
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
