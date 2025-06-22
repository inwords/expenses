import {UseCase} from '#packages/use-case';

import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {IEvent} from '#domain/entities/event.entity';
import {IUser} from '#domain/entities/user.enitity';
import {UserValueObject} from '#domain/value-objects/user.value-object';
import {Injectable} from '@nestjs/common';

type Input = {users: Array<Omit<IUser, 'id' | 'eventId'>>} & {pinCode: IEvent['pinCode']; eventId: IEvent['id']};
type Output = Array<IUser>;

@Injectable()
export class SaveUsersToEventUseCase implements UseCase<Input, Output> {
  constructor(private readonly rDataService: RelationalDataServiceAbstract) {}

  public async execute({eventId, users, pinCode}: Input) {
    const [event] = await this.rDataService.event.findById(eventId);

    if (event.pinCode === pinCode) {
      const usersValue = users.map((u) => new UserValueObject({...u, eventId}).value);

      await this.rDataService.user.insert(usersValue);

      return usersValue;
    }
  }
}
