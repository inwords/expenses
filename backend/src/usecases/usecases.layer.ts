import {Module, Provider} from '@nestjs/common';
import {FrameworksLayer} from '#frameworks/frameworks.layer';
import {allUsersUseCases} from './users';
import {allCronUseCases} from './cron';
import {allHealthUseCases} from './health';
import {allDevtoolsUseCases} from './devtools';
import {allSharedUseCases} from './shared';

const allUseCases: Provider[] = [
  ...allUsersUseCases,
  ...allCronUseCases,
  ...allHealthUseCases,
  ...allDevtoolsUseCases,
  ...allSharedUseCases,
];
@Module({
  imports: [FrameworksLayer],
  providers: [...allUseCases],
  exports: [...allUseCases],
})
export class UseCasesModule {}
