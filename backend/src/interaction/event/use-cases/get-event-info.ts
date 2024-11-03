import {UseCase} from '../../../packages/use-case';
import {Event} from '../../../domain/event/types';
import {IFindEvent} from '../../../persistence/event/types';
import {Inject} from '@nestjs/common';
import {FindEvent} from '../../../persistence/event/queries/find-event';
import {IFindUsers} from '../../../persistence/user/types';
import {FindUsers} from '../../../persistence/user/queries/find-users';

type Input = {eventId: number; pinCode: string};
type Output = Event;

export class GetEventInfo implements UseCase<Input, Output> {
  constructor(
    @Inject(FindEvent)
    private readonly findEvent: IFindEvent,
    @Inject(FindUsers)
    private readonly findUsers: IFindUsers,
  ) {}

  public async execute({eventId, pinCode}: Input) {
    const event = await this.findEvent.execute({eventId});

    if (pinCode === event.pinCode) {
      const users = await this.findUsers.execute({eventId});

      return {...event, users};
    }
  }
}
