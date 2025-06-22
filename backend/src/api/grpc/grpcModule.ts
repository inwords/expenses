import {Module} from '@nestjs/common';
import {UserController} from '#api/grpc/user/user.controller';
import {UseCasesModule} from '#usecases/usecases.layer';

@Module({
  imports: [UseCasesModule],
  controllers: [UserController],
})
export class GrpcModule {}
