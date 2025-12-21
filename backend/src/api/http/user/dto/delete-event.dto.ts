import {ApiProperty} from '@nestjs/swagger';
import {IsString, MaxLength} from 'class-validator';

export class DeleteEventBodyDto {
  @ApiProperty()
  @IsString()
  @MaxLength(4)
  pinCode!: string;
}
