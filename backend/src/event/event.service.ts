import {InjectEntityManager} from '@nestjs/typeorm';
import {EntityManager} from 'typeorm';
import {Event} from './event.entity';
import {Injectable} from '@nestjs/common';
import {CrateEventBodyDto} from './dto/event';
import {UserService} from '../user/user.service';
import {User} from '../user/user.entity';

@Injectable()
export class EventService {
  constructor(
    @InjectEntityManager() private readonly entityManager: EntityManager,
    private readonly userService: UserService,
  ) {}

  public async saveEvent(event: CrateEventBodyDto) {
    return await this.entityManager.transaction(async (entityManager) => {
      const eventWithoutUsers = await entityManager.getRepository(Event).save(event);
      const users = await this.userService.saveUsers(
        event.users.map((u) => {
          return {...u, eventId: eventWithoutUsers.id};
        }),
        entityManager
      );

      return {...eventWithoutUsers, users};
    });
  }

  public async getEventInfo(eventId: number, pinCode: string) {
    const event = await this.entityManager.getRepository(Event).findOne({
      where: {
        id: eventId,
      },
    });

    if (pinCode === event.pinCode) {
      const users = await this.entityManager.getRepository(User).find({
        where: {
          eventId,
        },
      });

      return {...event, users};
    }
  }

  public async addUsersToEvent(eventId: number, users: Array<Omit<User, 'id' | 'eventId'>>) {
    return this.userService.saveUsers(
      users.map((u) => {
        return {...u, eventId};
      }),
    );
  }
}
