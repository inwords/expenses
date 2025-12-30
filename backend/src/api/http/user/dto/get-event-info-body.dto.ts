import {IsString, MaxLength} from 'class-validator';
import {ApiProperty} from '@nestjs/swagger';

export class GetEventInfoBodyDto {
  @ApiProperty({description: 'Event PIN code', example: '1234'})
  @IsString()
  @MaxLength(4)
  pinCode: string;
}
