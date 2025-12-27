import {Module} from '@nestjs/common';
import {TerminusModule} from '@nestjs/terminus';
import {UserController} from './user/user.controller';
import {HealthController} from './health/health.controller';
import {DevtoolsController} from './devtools/devtools.controller';
import {UseCasesModule} from '#usecases/usecases.layer';

@Module({
  imports: [UseCasesModule, TerminusModule],
  controllers: [UserController, HealthController, DevtoolsController],
})
export class HttpModule {}
