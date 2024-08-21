import {Box, Tab, Tabs} from '@mui/material';
import {ExpensesList} from '@/3-widgets/ExpensesList/ExpensesList';
import {expenseStore} from '@/5-entities/expense/stores/expense-store';
import {expenseService} from '@/5-entities/expense/services/expense-service';
import {observer} from 'mobx-react-lite';
import {EventUsers} from '@/3-widgets/EventUsers/EventUsers';

export const EventTabs = observer(() => {
  return (
    <>
      <Box sx={{borderBottom: 1, borderColor: 'divider'}}>
        <Tabs
          value={expenseStore.currentTab}
          onChange={(_, v) => {
            expenseService.setCurrentTab(v);
          }}
          variant="scrollable"
          scrollButtons="auto"
        >
          <Tab label="Мои траты" value={0} />

          <Tab label="Общие траты" value={1} />

          <Tab label="Мои задолжности" value={2} />

          <Tab label="Мои поступления" value={3} />

          <Tab label="Участники поездки" value={4} />
        </Tabs>
      </Box>

      {expenseStore.currentTab === 4 ? <EventUsers /> : <ExpensesList />}
    </>
  );
});