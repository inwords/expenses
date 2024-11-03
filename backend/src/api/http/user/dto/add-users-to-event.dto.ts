import {ApiProperty} from "@nestjs/swagger";
import {IsString, MaxLength, ValidateNested} from "class-validator";
import {Type} from "class-transformer";
import {UserDto} from "./user.dto";
import {User} from "#domain/user/types";

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