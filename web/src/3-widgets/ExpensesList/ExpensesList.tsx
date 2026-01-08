import {Box, Button, Card, CardActions, CardContent, Stack, Typography} from '@mui/material';
import {observer} from 'mobx-react-lite';
import {expenseStore} from '@/5-entities/expense/stores/expense-store';
import {userStore} from '@/5-entities/user/stores/user-store';
import {currencyStore} from '@/5-entities/currency/stores/currency-store';
import {eventStore} from '@/5-entities/event/stores/event-store';

export const ExpensesList = observer(() => {
  const getExpenses = () => {
    if (expenseStore.currentTab === 0) {
      return expenseStore.currentUserExpenses;
    }

    if (expenseStore.currentTab === 1) {
      return expenseStore.expensesToView;
    }

    return [];
  };

  return (
    <Box display="flex" justifyContent={'center'} padding={'0 10px'}>
      <Stack minWidth={300} maxWidth={540} spacing={2} width="100%">
        {getExpenses().map((e) => {
          return (
            <Card key={e.id}>
              <CardContent>
                <Typography variant="h5">
                  <Stack direction="row" justifyContent={'space-between'}>
                    {e.description}

                    <div>
                      {e.amount} {currencyStore.getCurrencyCode(eventStore.currentEvent?.currencyId)}
                    </div>
                  </Stack>
                </Typography>

                <Typography variant="body2">{e.createdAt}</Typography>
              </CardContent>

              <CardActions>
                {userStore.currentUser?.id !== e.userWhoPaidId && (
                  <Button
                    variant="contained"
                    onClick={() => {
                      expenseStore.setCurrentExpenseRefund({
                        description: `Возврат за ${e.description}`,
                        amount: e.splitInformation.reduce((prev, curr) => {
                          if (curr.userId === userStore.currentUser?.id) {
                            prev += +curr.exchangedAmount;
                          }

                          return prev;
                        }, 0),
                        userWhoPaidId: userStore.currentUser?.id,
                        currencyId: eventStore.currentEvent?.currencyId,
                        userWhoReceiveId: e.userWhoPaidId,
                      });
                      expenseStore.setIsExpenseRefundModalOpen(true);
                    }}
                  >
                    Вернуть
                  </Button>
                )}
              </CardActions>
            </Card>
          );
        })}
      </Stack>
    </Box>
  );
});
