export enum OnboardingStep {
  // MainPage steps
  EVENT_ID = 'event_id',
  PIN_CODE = 'pin_code',
  PRIVACY_WARNING = 'privacy_warning',

  // EventPage steps
  EVENT_TABS = 'event_tabs',
  ADD_EXPENSE = 'add_expense',
  VIEW_DEBTS = 'view_debts',
  CHANGE_USER = 'change_user',
}

const ONBOARDING_KEY = 'commonex_onboarding';

interface OnboardingState {
  completedSteps: OnboardingStep[];
}

class OnboardingService {
  private readonly storageKey = ONBOARDING_KEY;

  private getState(): OnboardingState {
    try {
      const stored = localStorage.getItem(this.storageKey);
      if (!stored) {
        return {completedSteps: []};
      }
      return JSON.parse(stored);
    } catch {
      return {completedSteps: []};
    }
  }

  private saveState(state: OnboardingState): void {
    localStorage.setItem(this.storageKey, JSON.stringify(state));
  }

  completeStep(step: OnboardingStep): void {
    const state = this.getState();
    if (!state.completedSteps.includes(step)) {
      state.completedSteps.push(step);
      this.saveState(state);
    }
  }

  getCompletedSteps(): OnboardingStep[] {
    return this.getState().completedSteps;
  }
}

export const onboardingService = new OnboardingService();
