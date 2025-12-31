import {ApiProperty} from '@nestjs/swagger';
import {IsString, Length, ValidateNested} from 'class-validator';
import {Type} from 'class-transformer';
import {UserDto} from './user.dto';
import {IUserInfo} from '#domain/entities/user-info.entity';

export class CrateEventBodyDto {
  @ApiProperty()
  @IsString()
  name!: string;

  @ApiProperty()
  @IsString()
  currencyId!: string;

  @ApiProperty({isArray: true, type: UserDto})
  @ValidateNested()
  @Type(() => UserDto)
  users!: Array<Omit<IUserInfo, 'id' | 'eventId'>>;

  @ApiProperty()
  @IsString()
  @Length(4, 4)
  pinCode!: string;
}
