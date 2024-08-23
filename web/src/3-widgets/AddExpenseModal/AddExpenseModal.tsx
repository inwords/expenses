import {Dialog, DialogContent, DialogTitle} from '@mui/material';
import {AddExpenseForm} from '@/4-features/CreateExpense/ui/AddExpenseForm';
import {expenseService} from '@/5-entities/expense/services/expense-service';

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
            await expenseService.createExpense(d, id);
          }}
        />
      </DialogContent>
    </Dialog>
  );
};
