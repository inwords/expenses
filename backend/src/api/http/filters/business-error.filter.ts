import {ExceptionFilter, Catch, ArgumentsHost} from '@nestjs/common';
import {Response} from 'express';
import {
  EventNotFoundError,
  EventDeletedError,
  InvalidPinCodeError,
  InvalidTokenError,
  TokenExpiredError,
  CurrencyNotFoundError,
  CurrencyRateNotFoundError,
} from '#domain/errors/errors';

type BusinessError =
  | EventNotFoundError
  | EventDeletedError
  | InvalidPinCodeError
  | InvalidTokenError
  | TokenExpiredError
  | CurrencyNotFoundError
  | CurrencyRateNotFoundError;

@Catch(
  EventNotFoundError,
  EventDeletedError,
  InvalidPinCodeError,
  InvalidTokenError,
  TokenExpiredError,
  CurrencyNotFoundError,
  CurrencyRateNotFoundError,
)
export class BusinessErrorFilter implements ExceptionFilter {
  catch(exception: BusinessError, host: ArgumentsHost): void {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse<Response>();

    response.status(exception.httpCode).json({
      statusCode: exception.httpCode,
      code: exception.code,
      message: exception.message,
    });
  }
}
