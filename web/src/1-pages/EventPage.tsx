import {SelectUserList} from '@/widgets/SelectUserList/SelectUserList';
import {Typography} from '@mui/material';
import {ExpensesTabs} from '@/widgets/ExpensesTabs/ExpensesTabs';
import {AddExpenseModal} from '@/widgets/AddExpenseModal/AddExpenseModal';
import {useEffect, useState} from 'react';
import {useParams} from 'react-router';
import {CreateExpense} from '@/features/CreateExpense/ui/CreateExpense';
import {expenseService} from '@/entities/expense/services/expense-service';

export const EventPage = () => {
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const {id} = useParams();

  useEffect(() => {
    if (id) {
      void expenseService.fetchExpenses(id);
    }
  }, []);
  return (
    <>
      <SelectUserList />

      <Typography variant="h3" align="center" marginBottom={'20px'}>
        Гигатрип
      </Typography>

      <ExpensesTabs />

      <CreateExpense setIsOpen={setIsDialogOpen} />

      <AddExpenseModal isOpen={isDialogOpen} setIsOpen={setIsDialogOpen} />
    </>
  );
};
