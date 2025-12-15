import {Module} from '@nestjs/common';
import {UserController} from './user/user.controller';
import {UseCasesModule} from '#usecases/usecases.layer';
import {HealthModule} from './health/health.module';

@Module({
  imports: [UseCasesModule, HealthModule],
  controllers: [UserController],
})
export class HttpModule {}
