import {Snackbar, Alert} from '@mui/material';
import {observer} from 'mobx-react-lite';
import {notificationStore} from '@/6-shared/stores/notification-store';

export const NotificationToast = observer(() => {
  const handleClose = () => {
    notificationStore.clearNotification();
  };

  return (
    <Snackbar
      open={!!notificationStore.currentNotification}
      autoHideDuration={6000}
      onClose={handleClose}
      anchorOrigin={{vertical: 'top', horizontal: 'center'}}
    >
      <Alert
        onClose={handleClose}
        severity={notificationStore.currentNotification?.type || 'success'}
        sx={{width: '100%'}}
      >
        {notificationStore.currentNotification?.message}
      </Alert>
    </Snackbar>
  );
});
