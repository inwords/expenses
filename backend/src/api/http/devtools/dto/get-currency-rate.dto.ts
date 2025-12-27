import {ApiProperty} from '@nestjs/swagger';
import {IsString} from 'class-validator';

export class GetCurrencyRateQueryDto {
  @ApiProperty({
    description: 'Date in YYYY-MM-DD format',
    example: '2025-12-27',
  })
  @IsString()
  date!: string;
}
