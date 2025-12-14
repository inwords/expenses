import {ApiProperty} from '@nestjs/swagger';
import {IsString, MaxLength} from 'class-validator';

export class DeleteEventDto {
  @ApiProperty({type: String})
  @IsString()
  @MaxLength(4)
  pinCode!: string;
}
