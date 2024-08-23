import {ApiProperty} from '@nestjs/swagger';
import {IsEnum, IsNumber, IsString, ValidateNested} from 'class-validator';
import {Type} from 'class-transformer';
import {SplitInfo} from '../types';
import {ExpenseType} from '../constants';

export class EventIdDto {
  @ApiProperty()
  @IsNumber()
  eventId!: number;
}

class SplitInfoDto {
  @ApiProperty()
  @IsNumber()
  userId!: number;

  @ApiProperty()
  @IsNumber()
  amount!: number;
}

export class ExpenseCreatedDto {
  @ApiProperty()
  @IsString()
  description!: string;

  @ApiProperty()
  @IsNumber()
  userWhoPaidId!: number;

  @ApiProperty()
  @IsNumber()
  currencyId!: number;

  @ApiProperty()
  @IsNumber()
  eventId!: number;

  @ApiProperty()
  @IsEnum(ExpenseType)
  expenseType: ExpenseType;

  @ApiProperty({isArray: true, type: SplitInfoDto})
  @ValidateNested()
  @Type(() => SplitInfoDto)
  splitInformation!: Array<SplitInfo>;
}
