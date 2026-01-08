import {OnboardingStep} from '@/6-shared/services/onboarding-service';

export interface OnboardingStepData {
  title: string;
  content: string;
  step: OnboardingStep;
}

export const MAIN_PAGE_ONBOARDING_STEPS: OnboardingStepData[] = [
  {
    title: 'ID события',
    content:
      'ID события — это уникальный идентификатор вашего мероприятия. Вы получите его после создания события. Используйте его для быстрого доступа к событию.',
    step: OnboardingStep.EVENT_ID,
  },
  {
    title: 'Пин-код',
    content:
      'Пин-код — это защитный код для доступа к событию. Поделитесь им только с участниками вашего мероприятия. Без пин-кода никто не сможет просмотреть или изменить данные о расходах.',
    step: OnboardingStep.PIN_CODE,
  },
  {
    title: 'Безопасность данных',
    content:
      '⚠️ Важно: Не храните в сервисе конфиденциальные данные, номера карт, пароли или другую чувствительную информацию. Сервис предназначен только для учёта общих расходов.',
    step: OnboardingStep.PRIVACY_WARNING,
  },
];

export const EVENT_PAGE_ONBOARDING_STEPS: OnboardingStepData[] = [
  {
    title: 'Вкладки события',
    content:
      'Используйте вкладки для навигации: "Мои траты" - только ваши расходы, "Общие траты" - все расходы события, "Мои задолжности" - кому вы должны, "Мои поступления" - кто должен вам, "Участники поездки" - список всех участников.',
    step: OnboardingStep.EVENT_TABS,
  },
  {
    title: 'Добавление расходов',
    content:
      'Нажмите кнопку "Добавить трату" чтобы записать новый расход. Укажите описание, сумму, валюту и выберите участников, с которыми делится расход.',
    step: OnboardingStep.ADD_EXPENSE,
  },
  {
    title: 'Просмотр задолженностей',
    content:
      'Во вкладке "Мои задолжности" вы увидите, кому и сколько должны. Нажмите "Вернуть" чтобы записать возврат долга.',
    step: OnboardingStep.VIEW_DEBTS,
  },
  {
    title: 'Смена пользователя',
    content:
      'Нажмите на аватар в правом верхнем углу чтобы сменить пользователя. Это полезно если несколько участников используют одно устройство.',
    step: OnboardingStep.CHANGE_USER,
  },
];
