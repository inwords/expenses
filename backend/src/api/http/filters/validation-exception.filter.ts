import {ExceptionFilter, Catch, ArgumentsHost, BadRequestException, HttpStatus} from '@nestjs/common';
import {Response} from 'express';
import {ErrorCode} from '#domain/errors/error-codes.enum';

@Catch(BadRequestException)
export class ValidationExceptionFilter implements ExceptionFilter {
  private formatMessage(msg: any): string {
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

  catch(exception: BadRequestException, host: ArgumentsHost) {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse<Response>();
    const exceptionResponse = exception.getResponse() as any;

    const validationErrors = this.formatMessage(exceptionResponse.message);

    response.status(HttpStatus.BAD_REQUEST).json({
      statusCode: HttpStatus.BAD_REQUEST,
      code: ErrorCode.VALIDATION_ERROR,
      message: validationErrors,
    });
  }
}
