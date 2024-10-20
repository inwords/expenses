import {ApiProperty} from "@nestjs/swagger";
import {UserDto} from "../../../user/dto/user";
import {IsString, MaxLength, ValidateNested} from "class-validator";
import {Type} from "class-transformer";
import {User} from "../../../user/user.entity";

export class AddUsersToEventDto {
    @ApiProperty({isArray: true, type: UserDto})
    @ValidateNested()
    @Type(() => UserDto)
    users!: Array<Omit<User, 'id' | 'eventId'>>;

    @ApiProperty({type: String})
    @IsString()
    @MaxLength(4)
    pinCode!: string;
}