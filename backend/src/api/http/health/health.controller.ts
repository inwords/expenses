import {Controller, Get} from '@nestjs/common';
import {HealthCheck, HealthCheckService, HealthIndicatorResult} from '@nestjs/terminus';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';

@Controller('health')
export class HealthController {
  constructor(
    private health: HealthCheckService,
    private relationalDataService: RelationalDataServiceAbstract,
  ) {}

  @Get()
  @HealthCheck()
  check() {
    return this.health.check([
      () => this.databaseHealthCheck(),
    ]);
  }

  private async databaseHealthCheck(): Promise<HealthIndicatorResult> {
    await this.relationalDataService.healthCheck();
    return {database: {status: 'up'}};
  }
}
