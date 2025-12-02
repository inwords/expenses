import {UseCase} from '#packages/use-case';

import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {IEvent} from '#domain/entities/event.entity';
import {IUser} from '#domain/entities/user.enitity';
import {UserValueObject} from '#domain/value-objects/user.value-object';
import {HttpException, HttpStatus, Injectable} from '@nestjs/common';
import {ensureEventAvailable} from './utils/event-availability';

type Input = {users: Array<Omit<IUser, 'id' | 'eventId'>>} & {pinCode: IEvent['pinCode']; eventId: IEvent['id']};
type Output = Array<IUser>;

@Injectable()
export class SaveUsersToEventUseCase implements UseCase<Input, Output> {
  constructor(private readonly rDataService: RelationalDataServiceAbstract) {}

  public async execute({eventId, users, pinCode}: Input) {
    return this.rDataService.transaction(async (ctx) => {
      const event = await ensureEventAvailable(this.rDataService, eventId, {
        ctx,
        lock: 'pessimistic_write',
      });

      if (event.pinCode !== pinCode) {
        throw new HttpException(
          {
            status: HttpStatus.FORBIDDEN,
            error: 'Invalid event pin code',
          },
          HttpStatus.FORBIDDEN,
        );
      }

      const usersValue = users.map((u) => new UserValueObject({...u, eventId}).value);

      await this.rDataService.user.insert(usersValue, {ctx});

      return usersValue;
    });
  }
}
