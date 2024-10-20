import {ApiProperty} from "@nestjs/swagger";
import {IsNumber, IsString, ValidateNested} from "class-validator";
import {UserDto} from "../../../user/dto/user";
import {Type} from "class-transformer";
import {User} from "../../../user/user.entity";

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