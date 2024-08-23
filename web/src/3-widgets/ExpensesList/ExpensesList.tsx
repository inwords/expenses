import {Box, Button, Card, CardActions, CardContent, Stack, Typography} from '@mui/material';
import {observer} from 'mobx-react-lite';
import {expenseStore} from '@/5-entities/expense/stores/expense-store';
import {userStore} from '@/5-entities/user/stores/user-store';
import {CURRENCIES_ID_TO_CURRENCY_CODE} from '@/5-entities/currency/constants';

export const ExpensesList = observer(() => {
  const getExpences = () => {
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
        {getExpences().map((e) => {
          return (
            <Card key={e.id}>
              <CardContent>
                <Typography variant="h5">
                  <Stack direction="row" justifyContent={'space-between'}>
                    {e.description}

                    <div>
                      {e.amount} {CURRENCIES_ID_TO_CURRENCY_CODE[String(e.currencyId)]}
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
                            prev += +curr.amount;
                          }

                          return prev;
                        }, 0),
                        userWhoPaidId: userStore.currentUser?.id,
                        currencyId: e.currencyId,
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
