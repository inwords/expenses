import {Dialog, DialogContent, DialogTitle} from '@mui/material';
import {AddExpenseForm} from '@/features/CreateExpense/ui/AddExpenseForm';

interface Props {
  isOpen: boolean;
  setIsOpen: (status: boolean) => void;
}

export const AddExpenseModal = ({isOpen, setIsOpen}: Props) => {
  return (
    <Dialog open={isOpen} fullWidth={true} onClose={() => setIsOpen(false)}>
      <DialogTitle id="alert-dialog-title">Добавление траты</DialogTitle>

      <DialogContent>
        <AddExpenseForm />
      </DialogContent>
    </Dialog>
  );
};
