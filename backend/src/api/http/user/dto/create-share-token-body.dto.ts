import {IsString, Length} from 'class-validator';
import {ApiProperty} from '@nestjs/swagger';

export class CreateShareTokenBodyDto {
  @ApiProperty({description: 'Event PIN code', example: '1234'})
  @IsString()
  @Length(4, 4)
  pinCode: string;
}
