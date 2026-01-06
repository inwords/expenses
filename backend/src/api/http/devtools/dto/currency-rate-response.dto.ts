import {ApiProperty} from '@nestjs/swagger';

export class CurrencyRateResponseDto {
  @ApiProperty()
  date!: string;

  @ApiProperty({type: 'object', additionalProperties: {type: 'number'}})
  rate!: Record<string, number>;

  @ApiProperty()
  createdAt!: Date;

  @ApiProperty()
  updatedAt!: Date;
}
