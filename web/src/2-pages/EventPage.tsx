import {SelectUserList} from '@/3-widgets/SelectUserList/SelectUserList';
import {Typography} from '@mui/material';
import {ExpensesTabs} from '@/3-widgets/ExpensesTabs/ExpensesTabs';
import {AddExpenseModal} from '@/3-widgets/AddExpenseModal/AddExpenseModal';
import {useEffect, useState} from 'react';
import {useParams} from 'react-router';
import {CreateExpense} from '@/4-features/CreateExpense/ui/CreateExpense';
import {expenseService} from '@/5-entities/expense/services/expense-service';
import {userStore} from '@/5-entities/user/stores/user-store';
import {observer} from 'mobx-react-lite';
import {eventStore} from '@/5-entities/event/stores/event-store';

export const EventPage = observer(() => {
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

      {userStore.currentUser && (
        <>
          <Typography variant="h3" align="center" marginBottom={'20px'}>
            {eventStore.currentEventName}
          </Typography>

          <ExpensesTabs />

          <CreateExpense setIsOpen={setIsDialogOpen} />

          <AddExpenseModal isOpen={isDialogOpen} setIsOpen={setIsDialogOpen} />
        </>
      )}
    </>
  );
});
