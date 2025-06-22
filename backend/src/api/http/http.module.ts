import {Module} from '@nestjs/common';
import {UserController} from './user/user.controller';
import {UseCasesModule} from '#usecases/usecases.layer';

@Module({
  imports: [UseCasesModule],
  controllers: [UserController],
})
export class HttpModule {}
