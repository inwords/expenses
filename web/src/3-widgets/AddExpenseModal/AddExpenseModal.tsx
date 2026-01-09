import {Dialog, DialogContent, DialogTitle} from '@mui/material';
import {AddExpenseForm} from '@/4-features/CreateExpense/ui/AddExpenseForm';
import {expenseService} from '@/5-entities/expense/services/expense-service';
import {eventStore} from '@/5-entities/event/stores/event-store';

interface Props {
  isOpen: boolean;
  setIsOpen: (status: boolean) => void;
}

export const AddExpenseModal = ({isOpen, setIsOpen}: Props) => {
  return (
    <Dialog open={isOpen} fullWidth={true} onClose={() => setIsOpen(false)}>
      <DialogTitle id="alert-dialog-title">Добавление траты</DialogTitle>

      <DialogContent>
        <AddExpenseForm
          onSuccess={async (isOpen, d, id) => {
            setIsOpen(isOpen);
            if (eventStore.currentEvent?.pinCode) {
              await expenseService.createExpense(d, id, eventStore.currentEvent.pinCode);
            }
          }}
        />
      </DialogContent>
    </Dialog>
  );
};
