import {Module} from '@nestjs/common';
import {ApiModule} from '#api/api.layer';

@Module({
  imports: [ApiModule],
})
export class AppModule {}
