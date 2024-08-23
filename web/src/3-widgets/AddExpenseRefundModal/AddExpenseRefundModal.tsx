import {Dialog, DialogContent, DialogTitle} from '@mui/material';
import {AddExpenseFormRefund} from '@/4-features/CreateExpenseRefund/ui/AddExpenseFormRefund';
import {expenseStore} from '@/5-entities/expense/stores/expense-store';
import {observer} from 'mobx-react-lite';

export const AddExpenseRefundModal = observer(() => {
  return (
    <Dialog
      open={expenseStore.isExpenseRefundModalOpen}
      fullWidth={true}
      onClose={() => expenseStore.setIsExpenseRefundModalOpen(false)}
    >
      <DialogTitle id="alert-dialog-title">Возврат траты</DialogTitle>

      <DialogContent>
        <AddExpenseFormRefund />
      </DialogContent>
    </Dialog>
  );
});
