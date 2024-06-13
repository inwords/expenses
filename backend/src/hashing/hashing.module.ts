import {Module} from '@nestjs/common';
import {HashingService} from './hashing.service';
import {ConfigModule} from "@nestjs/config";

@Module({
  providers: [HashingService],
  exports: [HashingService],
  imports: [ConfigModule]
})
export class HashingModule {}
