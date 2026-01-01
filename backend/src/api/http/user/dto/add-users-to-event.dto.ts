import {ApiProperty} from '@nestjs/swagger';
import {IsString, Length, ValidateNested} from 'class-validator';
import {Type} from 'class-transformer';
import {UserDto} from './user.dto';
import {IUserInfo} from '#domain/entities/user-info.entity';

export class AddUsersToEventDto {
  @ApiProperty({isArray: true, type: UserDto})
  @ValidateNested()
  @Type(() => UserDto)
  users!: Array<Omit<IUserInfo, 'id' | 'eventId'>>;

  @ApiProperty({type: String})
  @IsString()
  @Length(4, 4)
  pinCode!: string;
}
