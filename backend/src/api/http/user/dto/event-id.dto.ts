import {ApiProperty} from '@nestjs/swagger';
import {IsString} from 'class-validator';

export class EventIdDto {
  @ApiProperty()
  @IsString()
  eventId!: string;
}
