import {Module} from '@nestjs/common';
import {TerminusModule} from '@nestjs/terminus';
import {UserController} from './user/user.controller';
import {HealthController} from './health/health.controller';
import {UseCasesModule} from '#usecases/usecases.layer';

@Module({
  imports: [UseCasesModule, TerminusModule],
  controllers: [UserController, HealthController],
})
export class HttpModule {}
