import {ExceptionFilter, Catch, ArgumentsHost, BadRequestException, HttpStatus} from '@nestjs/common';
import {Response} from 'express';
import {ErrorCode} from '#domain/errors/error-codes.enum';

@Catch(BadRequestException)
export class ValidationExceptionFilter implements ExceptionFilter {
  private formatMessage(msg: unknown): string {
    if (typeof msg === 'string') {
      return msg;
    }

    if (Array.isArray(msg)) {
      return msg.join('; ');
    }

    if (typeof msg === 'object' && msg !== null) {
      return JSON.stringify(msg);
    }

    return String(msg);
  }

  catch(exception: BadRequestException, host: ArgumentsHost): void {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse<Response>();
    const exceptionResponse = exception.getResponse() as {message: unknown};

    const validationErrors = this.formatMessage(exceptionResponse.message);

    response.status(HttpStatus.BAD_REQUEST).json({
      statusCode: HttpStatus.BAD_REQUEST,
      code: ErrorCode.VALIDATION_ERROR,
      message: validationErrors,
    });
  }
}
