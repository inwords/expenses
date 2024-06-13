import {InjectEntityManager} from '@nestjs/typeorm';
import {EntityManager} from 'typeorm';
import {Event} from './event.entity';
import {Injectable} from '@nestjs/common';
import {HashingService} from '../hashing/hashing.service';
import {CrateEventBodyDto} from './dto/event';

@Injectable()
export class EventService {
  constructor(
    @InjectEntityManager() private readonly entityManager: EntityManager,
    private readonly hashingService: HashingService,
  ) {}

  public async saveEvent(event: CrateEventBodyDto) {
    const mappedUsers = event.users.map((user, idx) => {
      return {...user, id: String(idx)};
    });

    const {pinCode, ...rest} = await this.entityManager
      .getRepository(Event)
      .save({...event, users: mappedUsers, pinCode: await this.hashingService.getHash(event.pinCode)});

    return rest;
  }

  public async getEventInfo(eventId: string, pinCode: string) {
    const {pinCode: hashedPinCode, ...rest} = await this.entityManager.getRepository(Event).findOne({
      where: {
        id: eventId,
      },
    });

    if (await this.hashingService.compare(pinCode, hashedPinCode)) {
      return rest;
    }
  }
}
