import {Module} from '@nestjs/common';
import {TerminusModule} from '@nestjs/terminus';
import {HealthController} from './health.controller';
import {FrameworksLayer} from '#frameworks/frameworks.layer';

@Module({
  imports: [TerminusModule, FrameworksLayer],
  controllers: [HealthController],
})
export class HealthModule {}
