import {Box, Button, Card, CardActions, CardContent, Stack, Typography} from '@mui/material';
import {observer} from 'mobx-react-lite';
import {expenseStore} from '@/5-entities/expense/stores/expense-store';

export const ExpensesList = observer(() => {
  return (
    <Box display="flex" justifyContent={'center'} padding={'0 10px'}>
      <Stack minWidth={300} maxWidth={540} spacing={2} width="100%">
        {expenseStore.expensesToView.map((e) => {
          return (
            <Card key={e.id}>
              <CardContent>
                <Typography variant="h5">
                  <Stack direction="row" justifyContent={'space-between'}>
                    {e.description}

                    <div>
                      {e.amount} {e.currencyId}
                    </div>
                  </Stack>
                </Typography>

                <Typography variant="body2">{e.createdAt}</Typography>
              </CardContent>

              <CardActions>
                <Button variant="contained">Вернуть</Button>
              </CardActions>
            </Card>
          );
        })}
      </Stack>
    </Box>
  );
});
