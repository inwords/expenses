import {HttpStatus} from '@nestjs/common';
import {ErrorCode} from './error-codes.enum';

export interface IBusinessError {
  httpStatus: HttpStatus;
  code: ErrorCode;
  message: string;
}

export const BUSINESS_ERRORS: Record<ErrorCode, IBusinessError> = {
  [ErrorCode.EVENT_NOT_FOUND]: {
    httpStatus: HttpStatus.NOT_FOUND,
    code: ErrorCode.EVENT_NOT_FOUND,
    message: 'Event not found',
  },
  [ErrorCode.EVENT_ALREADY_DELETED]: {
    httpStatus: HttpStatus.GONE,
    code: ErrorCode.EVENT_ALREADY_DELETED,
    message: 'Event has already been deleted',
  },
  [ErrorCode.EVENT_INVALID_PIN]: {
    httpStatus: HttpStatus.FORBIDDEN,
    code: ErrorCode.EVENT_INVALID_PIN,
    message: 'Invalid PIN code',
  },
  [ErrorCode.CURRENCY_NOT_FOUND]: {
    httpStatus: HttpStatus.BAD_REQUEST,
    code: ErrorCode.CURRENCY_NOT_FOUND,
    message: 'Currency not found',
  },
  [ErrorCode.CURRENCY_RATE_NOT_FOUND]: {
    httpStatus: HttpStatus.NOT_FOUND,
    code: ErrorCode.CURRENCY_RATE_NOT_FOUND,
    message: 'Currency rate not found',
  },
  [ErrorCode.VALIDATION_ERROR]: {
    httpStatus: HttpStatus.BAD_REQUEST,
    code: ErrorCode.VALIDATION_ERROR,
    message: 'Validation error',
  },
  [ErrorCode.INTERNAL_ERROR]: {
    httpStatus: HttpStatus.INTERNAL_SERVER_ERROR,
    code: ErrorCode.INTERNAL_ERROR,
    message: 'Internal server error',
  },
};
