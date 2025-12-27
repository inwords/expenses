import {Module, Provider} from '@nestjs/common';
import {FrameworksLayer} from '#frameworks/frameworks.layer';
import {allUsersUseCases} from './users';
import {allCronUseCases} from './cron';
import {allHealthUseCases} from './health';

const allUseCases: Provider[] = [...allUsersUseCases, ...allCronUseCases, ...allHealthUseCases];
@Module({
  imports: [FrameworksLayer],
  providers: [...allUseCases],
  exports: [...allUseCases],
})
export class UseCasesModule {}
