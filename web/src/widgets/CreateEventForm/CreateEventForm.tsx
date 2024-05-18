import {FormContainer} from 'react-hook-form-mui';
import {Button, Stack} from '@mui/material';

import React from 'react';
import {EventNameInput} from '@/features/CreateEvent/ui/EventNameInput';
import {EventUsers} from '@/features/CreateEvent/ui/EventUsers';
import {SelectCurrency} from '@/features/AddExpense/ui/SelectCurrency';
import {SelectEventOwner} from '@/features/CreateEvent/ui/SelectEventOwner';

export const CreateEventForm = () => {
  return (
    <FormContainer onSuccess={(d) => console.log(d)}>
      <Stack spacing={2} maxWidth={600}>
        <EventNameInput />

        <EventUsers />

        <SelectEventOwner />

        <SelectCurrency />
      </Stack>

      <Stack justifyContent="end" marginTop={'16px'}>
        <Button type={'submit'} variant="contained">
          Добавить трату
        </Button>
      </Stack>
    </FormContainer>
  );
};
