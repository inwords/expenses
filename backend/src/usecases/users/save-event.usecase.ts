import {UseCase} from '#packages/use-case';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {EventValueObject} from '#domain/value-objects/event.value-object';
import {IUser} from '#domain/entities/user.enitity';
import {IEvent} from '#domain/entities/event.entity';
import {UserValueObject} from '#domain/value-objects/user.value-object';
import {Injectable} from '@nestjs/common';

type Input = {users: Array<Omit<IUser, 'id' | 'eventId'>>; event: Pick<IEvent, 'name' | 'currencyId' | 'pinCode'>};
type Output = IEvent;

@Injectable()
export class SaveEventUseCase implements UseCase<Input, Output> {
  constructor(private readonly rDataService: RelationalDataServiceAbstract) {}

  public async execute({event, users}: Input) {
    return await this.rDataService.transaction(async (ctx) => {
      const eventValueObject = new EventValueObject(event);

      await this.rDataService.event.insert(eventValueObject.value, {ctx});

      const usersValue = users.map((u) => new UserValueObject({...u, eventId: eventValueObject.value.id}).value);

      await this.rDataService.user.insert(usersValue, {ctx});

      return {...eventValueObject.value, users: usersValue};
    });
  }
}
