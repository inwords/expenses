import {Button} from '@mui/material';
import {AddUsersToEventModal} from '@/3-widgets/AddUsersToEventModal/AddUsersToEventModal';
import {useState} from 'react';

export const AddUsersToEvent = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  return (
    <>
      <AddUsersToEventModal isOpen={isModalOpen} setIsOpen={setIsModalOpen} />

      <Button variant="outlined" onClick={() => setIsModalOpen(true)}>
        Добавить участника
      </Button>
    </>
  );
};
