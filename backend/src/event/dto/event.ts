import {ApiProperty} from '@nestjs/swagger';
import {IsNumber, IsString, MaxLength, ValidateNested} from 'class-validator';
import {Type} from 'class-transformer';
import {User} from '../../user/user.entity';
import {UserDto} from '../../user/dto/user';

export class CrateEventBodyDto {
  @ApiProperty()
  @IsString()
  name!: string;

  @ApiProperty()
  @IsNumber()
  currencyId!: number;

  @ApiProperty({isArray: true, type: UserDto})
  @ValidateNested()
  @Type(() => UserDto)
  users!: Array<Omit<User, 'id' | 'eventId'>>;

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
  @IsNumber()
  eventId!: number;
}

export class AddUsersToEventDto {
  @ApiProperty({isArray: true, type: UserDto})
  @ValidateNested()
  @Type(() => UserDto)
  users!: Array<Omit<User, 'id' | 'eventId'>>;
}
