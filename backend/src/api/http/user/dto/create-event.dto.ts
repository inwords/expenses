import {ApiProperty} from "@nestjs/swagger";
import {IsNumber, IsString, ValidateNested} from "class-validator";
import {Type} from "class-transformer";
import {UserDto} from "./user.dto";
import {User} from "#domain/user/types";

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