import {ApiProperty} from '@nestjs/swagger';
import {CurrencyCode} from '#domain/entities/currency.entity';

export class CurrencyResponseDto {
  @ApiProperty()
  id!: string;

  @ApiProperty({enum: CurrencyCode})
  code!: CurrencyCode;

  @ApiProperty()
  createdAt!: Date;

  @ApiProperty()
  updatedAt!: Date;
}

export class GetAllCurrenciesResponseDto {
  @ApiProperty({type: [CurrencyResponseDto]})
  currencies!: CurrencyResponseDto[];
}
