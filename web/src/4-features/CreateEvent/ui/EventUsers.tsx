import {TextFieldElement, useFieldArray, useForm} from 'react-hook-form-mui';
import React, {useEffect} from 'react';
import {Button, Stack} from '@mui/material';

export const EventUsers = () => {
  const {control, setValue} = useForm();

  const {fields, append} = useFieldArray({
    control, // control props comes from useForm (optional: if you are using FormProvider)
    name: 'users', // unique name for your Field Array
  });

  useEffect(() => {
    setValue('users', [{name: undefined}]);
  }, []);

  return (
    <Stack direction={'column'} spacing={2}>
      {fields.map((field, index) => {
        return (
          <React.Fragment key={field.id}>
            <TextFieldElement name={`users.${index}.name`} label={'Имя'} required />
          </React.Fragment>
        );
      })}

      <Button onClick={() => append({})} variant={'outlined'}>
        Добавить участника
      </Button>
    </Stack>
  );
};
