import {UseCase} from '#packages/use-case';
import {Event} from '#domain/event/types';
import {User} from '#domain/user/types';
import {InjectEntityManager} from '@nestjs/typeorm';
import {EntityManager} from 'typeorm';
import {Inject} from '@nestjs/common';
import {IFindEvent} from '#persistence/event/types';
import {IUpsertUsers} from '#persistence/user/types';
import {UpsertUsers} from '#persistence/user/queries/upsert-users';
import {FindEvent} from '#persistence/event/queries/find-event';

type Input = {users: Array<Omit<User, 'id' | 'eventId'>>} & Pick<Event, 'id' | 'pinCode'>;
type Output = Array<User>;

export class SaveUsersToEvent implements UseCase<Input, Output> {
  constructor(
    @InjectEntityManager() private readonly entityManager: EntityManager,
    @Inject(FindEvent) private readonly findEvent: IFindEvent,
    @Inject(UpsertUsers) private readonly upsertUsers: IUpsertUsers,
  ) {}

  public async execute({id, users, pinCode}: Input) {
    const event = await this.findEvent.execute({eventId: id});

    if (event.pinCode === pinCode) {
      return this.upsertUsers.execute({
        users: users.map((u) => {
          return {...u, eventId: id};
        }),
      });
    }
  }
}
