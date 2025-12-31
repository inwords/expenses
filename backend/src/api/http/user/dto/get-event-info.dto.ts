import {ApiProperty} from '@nestjs/swagger';
import {IsString, Length} from 'class-validator';

export class GetEventInfoQueryDto {
  @ApiProperty()
  @IsString()
  @Length(4, 4)
  pinCode!: string;
}
