import {ApiProperty} from "@nestjs/swagger";
import {IsNumber} from "class-validator";

export class EventIdDto {
    @ApiProperty()
    @IsNumber()
    eventId!: number;
}