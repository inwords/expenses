export interface ApiError {
  statusCode: number;
  code: string;
  message: string;
}

export enum ApiErrorCode {
  // Event errors
  EVENT_NOT_FOUND = 'B4001',
  EVENT_ALREADY_DELETED = 'B4002',
  EVENT_INVALID_PIN = 'B4003',

  // Currency errors
  CURRENCY_NOT_FOUND = 'B4004',
  CURRENCY_RATE_NOT_FOUND = 'B4005',

  // Generic errors
  VALIDATION_ERROR = 'B4006',
  INTERNAL_ERROR = 'B4007',

  // Token errors
  INVALID_TOKEN = 'B4008',
  TOKEN_EXPIRED = 'B4009',
}

export const ERROR_MESSAGES: Record<string, string> = {
  [ApiErrorCode.EVENT_NOT_FOUND]: 'Событие не найдено. Проверьте ID события.',
  [ApiErrorCode.EVENT_ALREADY_DELETED]: 'Событие уже удалено.',
  [ApiErrorCode.EVENT_INVALID_PIN]: 'Неверный пин-код. Проверьте введённые данные.',
  [ApiErrorCode.CURRENCY_NOT_FOUND]: 'Валюта не найдена.',
  [ApiErrorCode.CURRENCY_RATE_NOT_FOUND]: 'Курс валюты не найден. Попробуйте выбрать другую валюту.',
  [ApiErrorCode.VALIDATION_ERROR]: 'Ошибка валидации данных. Проверьте введённые данные.',
  [ApiErrorCode.INTERNAL_ERROR]: 'Произошла ошибка сервера. Попробуйте позже.',
  [ApiErrorCode.INVALID_TOKEN]: 'Недействительный токен. Попробуйте перезайти.',
  [ApiErrorCode.TOKEN_EXPIRED]: 'Токен истёк. Попробуйте перезайти.',
};

export function getUserFriendlyMessage(error: ApiError): string {
  return ERROR_MESSAGES[error.code] || error.message || 'Произошла непредвиденная ошибка.';
}
