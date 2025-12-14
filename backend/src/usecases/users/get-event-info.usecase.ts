import {UseCase} from '#packages/use-case';

import {IEvent} from '#domain/entities/event.entity';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {IUserInfo} from '#domain/entities/user-info.entity';
import {Injectable} from '@nestjs/common';

type Input = {eventId: string; pinCode: string};
type Output = IEvent & {users: Array<IUserInfo>};

@Injectable()
export class GetEventInfoUseCase implements UseCase<Input, Output> {
  constructor(private readonly rDataService: RelationalDataServiceAbstract) {}

  public async execute({eventId, pinCode}: Input) {
    const [event] = await this.rDataService.event.findById(eventId);

    if (pinCode === event.pinCode) {
      const [users] = await this.rDataService.userInfo.findByEventId(eventId);

      return {...event, users};
    }
  }
}
