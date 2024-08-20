import {SelectUserList} from '@/3-widgets/SelectUserList/SelectUserList';
import {Stack, Typography} from '@mui/material';
import {EventTabs} from '@/3-widgets/EventTabs/EventTabs';
import {AddExpenseModal} from '@/3-widgets/AddExpenseModal/AddExpenseModal';
import {useEffect, useState} from 'react';
import {useParams} from 'react-router';
import {CreateExpense} from '@/4-features/CreateExpense/ui/CreateExpense';
import {expenseService} from '@/5-entities/expense/services/expense-service';
import {userStore} from '@/5-entities/user/stores/user-store';
import {observer} from 'mobx-react-lite';
import {eventStore} from '@/5-entities/event/stores/event-store';
import {UserAvatar} from '@/4-features/SelectUser/ui/UserAvatar';

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
      {userStore.currentUser && (
        <Stack alignItems={'end'}>
          <UserAvatar letter={userStore.currentUser.name[0]} isSelected />
        </Stack>
      )}

      <SelectUserList />

      {userStore.currentUser && (
        <>
          <Typography variant="h3" align="center" marginBottom={'20px'}>
            {eventStore.currentEvent?.name}
          </Typography>

          <EventTabs />

          <CreateExpense setIsOpen={setIsDialogOpen} />

          <AddExpenseModal isOpen={isDialogOpen} setIsOpen={setIsDialogOpen} />
        </>
      )}
    </>
  );
});
