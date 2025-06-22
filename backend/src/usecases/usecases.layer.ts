import {Module, Provider} from '@nestjs/common';
import {FrameworksLayer} from '../frameworks/frameworks.layer';
import {allUsersUseCases} from './users';

const allUseCases: Provider[] = [...allUsersUseCases];
@Module({
  imports: [FrameworksLayer],
  providers: [...allUseCases],
  exports: [...allUseCases],
})
export class UseCasesModule {}
