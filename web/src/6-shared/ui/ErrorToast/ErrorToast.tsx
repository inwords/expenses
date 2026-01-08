import {Snackbar, Alert} from '@mui/material';
import {observer} from 'mobx-react-lite';
import {errorStore} from '@/6-shared/stores/error-store';

export const ErrorToast = observer(() => {
  const handleClose = () => {
    errorStore.clearError();
  };

  return (
    <Snackbar
      open={!!errorStore.currentError}
      autoHideDuration={6000}
      onClose={handleClose}
      anchorOrigin={{vertical: 'top', horizontal: 'center'}}
    >
      <Alert onClose={handleClose} severity="error" sx={{width: '100%'}}>
        {errorStore.errorMessage}
      </Alert>
    </Snackbar>
  );
});
