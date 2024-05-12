import {SelectUserList} from '@/widgets/SelectUserList/SelectUserList';
import {Typography} from '@mui/material';
import {ExpensesTabs} from '@/widgets/ExpensesTabs/ExpensesTabs';
import {CreateExpense} from '@/features/CreateExpense/CreateExpense';
import {CreateExpenseModal} from '@/widgets/CreateExpenseModal/CreateExpenseModal';
import {useState} from 'react';

export const EventPage = () => {
  const [isDialogOpen, setIsDialogOpen] = useState(false);

  return (
    <>
      <SelectUserList />

      <Typography variant="h3" align="center" marginBottom={'20px'}>
        Гигатрип
      </Typography>

      <ExpensesTabs />

      <CreateExpense setIsOpen={setIsDialogOpen} />

      <CreateExpenseModal isOpen={isDialogOpen} setIsOpen={setIsDialogOpen} />
    </>
  );
};
