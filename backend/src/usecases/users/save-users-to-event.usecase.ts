import {UseCase} from '#packages/use-case';

import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {IEvent} from '#domain/entities/event.entity';
import {IUserInfo} from '#domain/entities/user-info.entity';
import {UserInfoValueObject} from '#domain/value-objects/user-info.value-object';
import {Injectable} from '@nestjs/common';

type Input = {users: Array<Omit<IUserInfo, 'id' | 'eventId'>>} & {pinCode: IEvent['pinCode']; eventId: IEvent['id']};
type Output = Array<IUserInfo>;

@Injectable()
export class SaveUsersToEventUseCase implements UseCase<Input, Output> {
  constructor(private readonly rDataService: RelationalDataServiceAbstract) {}

  public async execute({eventId, users, pinCode}: Input) {
    const [event] = await this.rDataService.event.findById(eventId);

    if (event.pinCode === pinCode) {
      const usersValue = users.map((u) => new UserInfoValueObject({...u, eventId}).value);

      await this.rDataService.userInfo.insert(usersValue);

      return usersValue;
    }
  }
}
