import {makeAutoObservable} from 'mobx';
import {ApiError, getUserFriendlyMessage} from '@/6-shared/api/errors';

class ErrorStore {
  currentError: ApiError | null = null;

  constructor() {
    makeAutoObservable(this);
  }

  setError(error: ApiError): void {
    this.currentError = error;
  }

  clearError(): void {
    this.currentError = null;
  }

  get errorMessage(): string | null {
    return this.currentError ? getUserFriendlyMessage(this.currentError) : null;
  }
}

export const errorStore = new ErrorStore();
