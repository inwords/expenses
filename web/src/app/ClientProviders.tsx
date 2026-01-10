'use client';

import {ErrorToast} from '@/6-shared/ui/ErrorToast';
import {NotificationToast} from '@/6-shared/ui/NotificationToast';

export function ClientProviders() {
  return (
    <>
      <ErrorToast />
      <NotificationToast />
    </>
  );
}
