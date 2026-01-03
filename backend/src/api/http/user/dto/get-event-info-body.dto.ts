import {IsString, Length, ValidateIf} from 'class-validator';
import {ApiProperty} from '@nestjs/swagger';

export class GetEventInfoBodyDto {
  @ApiProperty({
    description: 'Event PIN code. Either pinCode or token must be provided.',
    example: '1234',
    required: false,
  })
  @ValidateIf((o) => !o.token)
  @IsString()
  @Length(4, 4)
  pinCode?: string;

  @ApiProperty({
    description: 'Event share token. Either pinCode or token must be provided.',
    example: 'abc123def456',
    required: false,
  })
  @ValidateIf((o) => !o.pinCode)
  @IsString()
  token?: string;
}
