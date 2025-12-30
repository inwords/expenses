import {IsString, MaxLength} from 'class-validator';
import {ApiProperty} from '@nestjs/swagger';
import {CreatedExpenseDto} from './create-expense.dto';

export class CreateExpenseV2Dto extends CreatedExpenseDto {
  @ApiProperty({description: 'Event PIN code', example: '1234'})
  @IsString()
  @MaxLength(4)
  pinCode: string;
}
