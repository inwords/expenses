import {Module, Provider} from '@nestjs/common';
import {ScheduleModule} from '@nestjs/schedule';
import {FrameworksLayer} from '../frameworks/frameworks.layer';
import {allUsersUseCases} from './users';

const allUseCases: Provider[] = [...allUsersUseCases];
@Module({
  imports: [FrameworksLayer, ScheduleModule.forRoot()],
  providers: [...allUseCases],
  exports: [...allUseCases],
})
export class UseCasesModule {}
