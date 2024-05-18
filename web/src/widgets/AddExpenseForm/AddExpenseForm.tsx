import {FormContainer, SelectElement, TextFieldElement, useFieldArray, useForm} from 'react-hook-form-mui';
import {Button, Stack} from '@mui/material';
import {ExpenseNameInput} from '@/features/AddExpense/ui/ExpenseNameInput';
import {ExpenseAmountInput} from '@/features/AddExpense/ui/ExpenseAmountInput';
import {SelectExpenseOwner} from '@/features/AddExpense/ui/SelectExpenseOwner';
import {SelectCurrency} from '@/features/AddExpense/ui/SelectCurrency';
import React from 'react';
import {SplitOptions} from '@/features/AddExpense/ui/SplitOption';

export const AddExpenseForm = () => {
  const {control} = useForm();
  const {fields, append} = useFieldArray({
    control, // control props comes from useForm (optional: if you are using FormProvider)
    name: 'users', // unique name for your Field Array
  });

  return (
    <FormContainer onSuccess={(d) => console.log(d)}>
      <Stack spacing={2} maxWidth={600}>
        <ExpenseNameInput />

        <ExpenseAmountInput />

        <SelectExpenseOwner />

        <SelectCurrency />

        <SplitOptions />

        {fields.map((field, index) => (
          <React.Fragment key={field.id}>
            <TextFieldElement name={`users.${index}.amount`} label={'Сумма к возврату'} required />

            <SelectElement
              label="Кто должен"
              name={`users.${index}.person`}
              options={[
                {
                  id: '1',
                  label: 'Ignat',
                },
                {
                  id: '2',
                  label: 'Vasya',
                },
                {
                  id: '3',
                  label: 'Dog',
                },
              ]}
            />
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
};
