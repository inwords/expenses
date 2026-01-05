import {Module} from '@nestjs/common';
import {HttpModule} from '#api/http/http.module';
import {GrpcModule} from '#api/grpc/grpcModule';
import {CronModule} from '#api/cron/cron.module';

@Module({
  imports: [HttpModule, GrpcModule, CronModule],
  controllers: [],
  providers: [],
})
export class ApiModule {}
