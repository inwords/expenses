import {ApiProperty} from '@nestjs/swagger';
import {IsString, MaxLength, ValidateNested} from 'class-validator';
import {User} from '../types';
import {Type} from 'class-transformer';

class UserDto {
  @ApiProperty()
  @IsString()
  name: string;

  @ApiProperty()
  @IsString()
  phone: string;
}

export class CrateEventBodyDto {
  @ApiProperty()
  @IsString()
  name!: string;

  @ApiProperty()
  @IsString()
  ownerId!: string;

  @ApiProperty()
  @IsString()
  currencyId!: string;

  @ApiProperty({isArray: true, type: UserDto})
  @ValidateNested()
  @Type(() => UserDto)
  users!: Array<Omit<User, 'id'>>;

  @ApiProperty()
  @IsString()
  pinCode!: string;
}

export class GetEventInfoQueryDto {
  @ApiProperty()
  @IsString()
  @MaxLength(4)
  pinCode!: string;
}

export class EventIdDto {
  @ApiProperty()
  @IsString()
  eventId!: string;
}
