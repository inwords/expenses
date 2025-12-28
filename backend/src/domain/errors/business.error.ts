import {HttpException} from '@nestjs/common';
import {IBusinessError} from './business-errors.const';

export class BusinessError extends HttpException {
  public readonly code: string;
  public readonly details?: Record<string, any>;

  constructor(
    businessError: IBusinessError,
    details?: Record<string, any>,
    customMessage?: string,
  ) {
    const message = customMessage || businessError.message;
    super(
      {
        statusCode: businessError.httpStatus,
        code: businessError.code,
        message,
        details,
      },
      businessError.httpStatus,
    );

    this.code = businessError.code;
    this.details = details;
  }
}
