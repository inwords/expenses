import {Box, Button, Card, CardActions, CardContent, Stack, Typography} from '@mui/material';

const EXPENSES = [
  {name: 'Пиво', date: new Date().toISOString(), amount: 200, currency: 'RUB', id: '1'},
  {name: 'Чипсеки', date: new Date().toISOString(), amount: 100, currency: 'RUB', id: '2'},
];
export const ExpensesList = () => {
  return (
    <Box display="flex" justifyContent={'center'} padding={'0 10px'}>
      <Stack minWidth={300} maxWidth={540} spacing={2} width="100%">
        {EXPENSES.map((e) => {
          return (
            <Card key={e.id}>
              <CardContent>
                <Typography variant="h5">
                  <Stack direction="row" justifyContent={'space-between'}>
                    {e.name}

                    <div>
                      {e.amount} {e.currency}
                    </div>
                  </Stack>
                </Typography>

                <Typography variant="body2">{e.date}</Typography>
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
};
