import {Injectable, CanActivate, ExecutionContext, UnauthorizedException} from '@nestjs/common';
import {Request} from 'express';
import {env} from '../../../../config';

@Injectable()
export class DevtoolsSecretGuard implements CanActivate {
  canActivate(context: ExecutionContext): boolean {
    const request = context.switchToHttp().getRequest<Request>();
    const secret = request.headers['x-devtools-secret'];

    if (!env.DEVTOOLS_SECRET) {
      throw new UnauthorizedException('Devtools secret is not configured');
    }

    if (!secret || secret !== env.DEVTOOLS_SECRET) {
      throw new UnauthorizedException('Invalid devtools secret');
    }

    return true;
  }
}
