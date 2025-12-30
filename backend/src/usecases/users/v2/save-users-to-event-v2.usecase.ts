import {UseCase} from '#packages/use-case';

import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {EventServiceAbstract} from '#domain/abstracts/event-service/event-service';
import {IEvent} from '#domain/entities/event.entity';
import {IUserInfo} from '#domain/entities/user-info.entity';
import {UserInfoValueObject} from '#domain/value-objects/user-info.value-object';
import {Injectable} from '@nestjs/common';

type Input = {users: Array<Omit<IUserInfo, 'id' | 'eventId'>>} & {pinCode: IEvent['pinCode']; eventId: IEvent['id']};
type Output = Array<IUserInfo>;

@Injectable()
export class SaveUsersToEventV2UseCase implements UseCase<Input, Output> {
  constructor(
    private readonly rDataService: RelationalDataServiceAbstract,
    private readonly eventService: EventServiceAbstract,
  ) {}

  public async execute({eventId, users, pinCode}: Input): Promise<Output> {
    return this.rDataService.transaction(async (ctx) => {
      // Блокируем event с pessimistic_write для предотвращения race condition
      // Если кто-то пытается удалить event, мы заблокируемся или получим ошибку (nowait)
      const [event] = await this.rDataService.event.findById(eventId, {
        ctx,
        lock: 'pessimistic_write',
        onLocked: 'nowait',
      });

      // Use EventService for consistent validation
      this.eventService.validateEvent(event, pinCode);

      const usersValue = users.map((u) => new UserInfoValueObject({...u, eventId}).value);

      await this.rDataService.userInfo.insert(usersValue, {ctx});

      return usersValue;
    });
  }
}
