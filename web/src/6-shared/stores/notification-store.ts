import {makeAutoObservable} from 'mobx';

export type NotificationType = 'success' | 'info' | 'warning' | 'error';

interface Notification {
  message: string;
  type: NotificationType;
}

class NotificationStore {
  currentNotification: Notification | null = null;

  constructor() {
    makeAutoObservable(this);
  }

  setNotification(message: string, type: NotificationType = 'success'): void {
    this.currentNotification = {message, type};
  }

  clearNotification(): void {
    this.currentNotification = null;
  }
}

export const notificationStore = new NotificationStore();
