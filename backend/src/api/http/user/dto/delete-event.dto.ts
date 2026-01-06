import {ApiProperty} from '@nestjs/swagger';
import {IsString, Length} from 'class-validator';

export class DeleteEventParamsDto {
  @ApiProperty()
  @IsString()
  eventId!: string;
}

export class DeleteEventRequestDto {
  @ApiProperty()
  @IsString()
  @Length(4, 4)
  pinCode!: string;
}

export class DeleteEventResponseDto {
  @ApiProperty()
  id!: string;

  @ApiProperty()
  deletedAt!: Date;
}
