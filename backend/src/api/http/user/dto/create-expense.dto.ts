import {ApiProperty} from '@nestjs/swagger';
import {IsDate, IsEnum, IsNumber, IsOptional, IsString, ValidateNested} from 'class-validator';
import {Type} from 'class-transformer';
import {ExpenseType} from '#domain/expense/constants';
import {SplitInfo} from '#domain/expense/types';

class SplitInfoDto {
  @ApiProperty()
  @IsNumber()
  userId!: number;

  @ApiProperty()
  @IsNumber()
  amount!: number;
}

export class CreatedExpenseDto {
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
  @IsEnum(ExpenseType)
  expenseType: ExpenseType;

  @ApiProperty({isArray: true, type: SplitInfoDto})
  @ValidateNested()
  @Type(() => SplitInfoDto)
  splitInformation!: Array<SplitInfo>;

  @ApiProperty({required: false})
  @IsOptional()
  @IsDate()
  @Type(() => Date)
  createdAt?: Date;
}
