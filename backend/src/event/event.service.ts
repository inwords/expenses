import {InjectEntityManager} from '@nestjs/typeorm';
import {EntityManager} from 'typeorm';
import {Event} from './event.entity';

export class EventService {
  constructor(@InjectEntityManager() private readonly entityManager: EntityManager) {}

  public saveEvent(event: Omit<Event, 'id' | 'currency'>) {
    return this.entityManager.getRepository(Event).save(event);
  }
}
