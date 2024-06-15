import {TextFieldElement, useFieldArray, useForm} from 'react-hook-form-mui';
import React from 'react';
import {Button} from '@mui/material';

export const EventUsers = () => {
  const {control} = useForm();

  const {fields, append} = useFieldArray({
    control, // control props comes from useForm (optional: if you are using FormProvider)
    name: 'users', // unique name for your Field Array
  });

  return (
    <>
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
    </>
  );
};
