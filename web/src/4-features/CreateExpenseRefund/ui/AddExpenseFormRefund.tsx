import {FormContainer} from 'react-hook-form-mui';
import {Button, Stack} from '@mui/material';
import React from 'react';
import {useParams} from 'react-router';
import {observer} from 'mobx-react-lite';
import {userStore} from '@/5-entities/user/stores/user-store';
import {CreateExpenseRefundForm} from '@/5-entities/expense/types/types';
import {SelectCurrency} from '@/5-entities/currency/ui/SelectCurrency';
import {ExpenseRefundDescriptionInput} from '@/4-features/AddExpenseRefund/ui/ExpenseNameInput';
import {ExpenseRefundAmountInput} from '@/4-features/AddExpenseRefund/ui/ExpenseRefundAmountInput';
import {SelectExpenseRefundOwner} from '@/4-features/AddExpenseRefund/ui/SelectExpenseRefundOwner';
import {SelectExpenseRefundReceiver} from '@/4-features/AddExpenseRefund/ui/SelectExpenseRefundReceiver';
import {expenseStore} from '@/5-entities/expense/stores/expense-store';
import {expenseService} from '@/5-entities/expense/services/expense-service';

export const AddExpenseFormRefund = observer(() => {
  const {id} = useParams();
  const initialValues = {
    ...expenseStore.currentExpenseRefund,
    userWhoPaidId: userStore.currentUser?.id,
  } as CreateExpenseRefundForm;

  return (
    <FormContainer
      onSuccess={async (d) => {
        if (id) {
          expenseStore.setIsExpenseRefundModalOpen(false);
          await expenseService.createExpenseRefund({...d, eventId: Number(id)});
        }
      }}
      defaultValues={initialValues}
    >
      <Stack spacing={2} maxWidth={600}>
        <ExpenseRefundDescriptionInput />

        <ExpenseRefundAmountInput />

        <SelectExpenseRefundOwner />

        <SelectExpenseRefundReceiver />

        <SelectCurrency />
      </Stack>

      <Stack justifyContent="end" marginTop={'16px'}>
        <Button type={'submit'} variant="contained">
          Отправить
        </Button>
      </Stack>
    </FormContainer>
  );
});
