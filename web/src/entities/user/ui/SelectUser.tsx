import {SelectElement} from 'react-hook-form-mui';
interface Props {
  name: string;
  label: string;
}

export const SelectUser = ({name, label}: Props) => {
  return (
    <SelectElement
      name={name}
      label={label}
      options={[
        {
          id: '1',
          label: 'Ignat',
        },
        {
          id: '2',
          label: 'Vasya',
        },
        {
          id: '3',
          label: 'Dog',
        },
      ]}
    />
  );
};
