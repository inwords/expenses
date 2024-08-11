import {useState} from 'react';
import {Box, Tab, Tabs} from '@mui/material';
import {ExpensesList} from '@/3-widgets/ExpensesList/ExpensesList';

export const ExpensesTabs = () => {
  const [tab, setTab] = useState(0);

  return (
    <>
      <Box sx={{borderBottom: 1, borderColor: 'divider'}}>
        <Tabs
          value={tab}
          onChange={(_, v) => {
            setTab(v);
          }}
        >
          <Tab label="Мои траты" />

          <Tab label="Общие траты" />

          <Tab label="Мои задолжности" />

          <Tab label="Мои поступления" />
        </Tabs>
      </Box>

      <ExpensesList />
    </>
  );
};
