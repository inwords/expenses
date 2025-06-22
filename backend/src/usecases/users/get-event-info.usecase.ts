import {UseCase} from '#packages/use-case';

import {IEvent} from '#domain/entities/event.entity';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {IUser} from '#domain/entities/user.enitity';
import {Injectable} from '@nestjs/common';

type Input = {eventId: string; pinCode: string};
type Output = IEvent & {users: Array<IUser>};

@Injectable()
export class GetEventInfoUseCase implements UseCase<Input, Output> {
  constructor(private readonly rDataService: RelationalDataServiceAbstract) {}

  public async execute({eventId, pinCode}: Input) {
    const [event] = await this.rDataService.event.findById(eventId);

    if (pinCode === event.pinCode) {
      const [users] = await this.rDataService.user.findByEventId(eventId);

      return {...event, users};
    }
  }
}
