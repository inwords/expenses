import {UseCase} from '#packages/use-case';

import {IEvent} from '#domain/entities/event.entity';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {EventServiceAbstract} from '#domain/abstracts/event-service/event-service';
import {IUserInfo} from '#domain/entities/user-info.entity';
import {Injectable} from '@nestjs/common';

type Input = {eventId: string; pinCode: string};
type Output = IEvent & {users: Array<IUserInfo>};

@Injectable()
export class GetEventInfoV2UseCase implements UseCase<Input, Output> {
  constructor(
    private readonly rDataService: RelationalDataServiceAbstract,
    private readonly eventService: EventServiceAbstract,
  ) {}

  public async execute({eventId, pinCode}: Input): Promise<Output> {
    const [event] = await this.rDataService.event.findById(eventId);

    this.eventService.validateEvent(event, pinCode);

    const [users] = await this.rDataService.userInfo.findByEventId(eventId);

    return {...event, users};
  }
}
