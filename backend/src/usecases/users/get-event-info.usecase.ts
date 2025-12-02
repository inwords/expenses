import {UseCase} from '#packages/use-case';

import {IEvent} from '#domain/entities/event.entity';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {IUser} from '#domain/entities/user.enitity';
import {HttpException, HttpStatus, Injectable} from '@nestjs/common';
import {ensureEventAvailable} from './utils/event-availability';

type Input = {eventId: string; pinCode: string};
type Output = IEvent & {users: Array<IUser>};

@Injectable()
export class GetEventInfoUseCase implements UseCase<Input, Output> {
  constructor(private readonly rDataService: RelationalDataServiceAbstract) {}

  public async execute({eventId, pinCode}: Input) {
    const event = await ensureEventAvailable(this.rDataService, eventId);

    if (pinCode !== event.pinCode) {
      throw new HttpException(
        {
          status: HttpStatus.FORBIDDEN,
          error: 'Invalid event pin code',
        },
        HttpStatus.FORBIDDEN,
      );
    }

    const [users] = await this.rDataService.user.findByEventId(eventId);

    return {...event, users};
  }
}
