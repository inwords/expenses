import {ApiProperty} from '@nestjs/swagger';
import {IsString, ValidateNested} from 'class-validator';
import {Type} from 'class-transformer';
import {UserDto} from './user.dto';
import {IUser} from '#domain/entities/user.enitity';

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
  users!: Array<Omit<IUser, 'id' | 'eventId'>>;

  @ApiProperty()
  @IsString()
  pinCode!: string;
}
