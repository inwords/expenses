import {FormContainer} from 'react-hook-form-mui';
import {Button, Stack} from '@mui/material';

import React from 'react';
import {EventNameInput} from '@/4-features/CreateEvent/ui/EventNameInput';
import {EventUsers} from '@/4-features/CreateEvent/ui/EventUsers';
import {SelectCurrency} from '@/4-features/AddExpense/ui/SelectCurrency';
import {createEvent} from '@/5-entities/event/services/api';
import {EventPinCodeInput} from '@/5-entities/event/ui/EventPinCodeInput';
import {useNavigate} from 'react-router';
import {ROUTES} from '@/6-shared/routing/constants';

export const CreateEventForm = () => {
  const navigate = useNavigate();

  return (
    <FormContainer
      onSuccess={async (data) => {
        const resp = await createEvent(data);

        navigate(ROUTES.Event(resp.id));
      }}
    >
      <Stack spacing={2} maxWidth={600}>
        <EventNameInput />

        <EventPinCodeInput />

        <EventUsers />

        <SelectCurrency />
      </Stack>

      <Stack justifyContent="end" marginTop={'16px'}>
        <Button type={'submit'} variant="contained">
          Создать поездку
        </Button>
      </Stack>
    </FormContainer>
  );
};
