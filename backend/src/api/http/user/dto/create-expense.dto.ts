import {ApiProperty} from '@nestjs/swagger';
import {IsDate, IsEnum, IsNumber, IsOptional, IsString, ValidateNested} from 'class-validator';
import {Type} from 'class-transformer';

import {ExpenseType, ISplitInfo} from '#domain/entities/expense.entity';

class SplitInfoDto {
  @ApiProperty()
  @IsString()
  userId!: string;

  @ApiProperty()
  @IsNumber()
  amount!: number;
}

export class CreatedExpenseDto {
  @ApiProperty()
  @IsString()
  description!: string;

  @ApiProperty()
  @IsString()
  userWhoPaidId!: string;

  @ApiProperty()
  @IsString()
  currencyId!: string;

  @ApiProperty()
  @IsEnum(ExpenseType)
  expenseType: ExpenseType;

  @ApiProperty({isArray: true, type: SplitInfoDto})
  @ValidateNested()
  @Type(() => SplitInfoDto)
  splitInformation!: Array<ISplitInfo>;

  @ApiProperty({required: false, description: 'ISO String'})
  @IsOptional()
  @IsDate()
  @Type(() => Date)
  createdAt?: Date;
}
