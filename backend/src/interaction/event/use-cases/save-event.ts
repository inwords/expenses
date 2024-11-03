import {UseCase} from '#packages/use-case';
import {Event} from '#domain/event/types';
import {User} from '#domain/user/types';
import {InjectEntityManager} from '@nestjs/typeorm';
import {EntityManager} from 'typeorm';
import {Inject} from '@nestjs/common';
import {IUpsertEvent} from '#persistence/event/types';
import {UpsertEvent} from '#persistence/event/queries/upsert-event';
import {IUpsertUsers} from '#persistence/user/types';
import {UpsertUsers} from '#persistence/user/queries/upsert-users';

type Input = {users: Array<Omit<User, 'id' | 'eventId'>>; event: Pick<Event, 'name' | 'currencyId' | 'pinCode'>};
type Output = Event;

export class SaveEvent implements UseCase<Input, Output> {
  constructor(
    @InjectEntityManager() private readonly entityManager: EntityManager,
    @Inject(UpsertEvent) private readonly upsertEvent: IUpsertEvent,
    @Inject(UpsertUsers) private readonly upsertUsers: IUpsertUsers,
  ) {}

  public async execute({event, users}: Input) {
    return await this.entityManager.transaction(async (entityManager) => {
      const eventWithoutUsers = await this.upsertEvent.execute(event);
      const savedUsers = await this.upsertUsers.execute({
        entityManager,
        users: users.map((u) => {
          return {...u, eventId: eventWithoutUsers.id};
        }),
      });

      return {...eventWithoutUsers, users: savedUsers};
    });
  }
}
