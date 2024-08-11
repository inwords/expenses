import {Dialog, DialogContent, DialogTitle} from '@mui/material';
import {CreateEventForm} from "@/4-features/CreateEvent/ui/CreateEventForm";

interface Props {
  isOpen: boolean;
  setIsOpen: (status: boolean) => void;
}

export const CreateEventModal = ({isOpen, setIsOpen}: Props) => {
  return (
    <Dialog open={isOpen} fullWidth={true} onClose={() => setIsOpen(false)}>
      <DialogTitle id="alert-dialog-title">Создание поездки</DialogTitle>

      <DialogContent>
        <CreateEventForm />
      </DialogContent>
    </Dialog>
  );
};
