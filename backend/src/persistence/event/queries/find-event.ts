import {FindEventInput, IFindEvent} from '../types';
import {InjectEntityManager} from '@nestjs/typeorm';
import {EntityManager} from 'typeorm';
import {Event} from '#persistence/entities/event.entity';
import {Injectable} from "@nestjs/common";

@Injectable()
export class FindEvent implements IFindEvent {
  constructor(@InjectEntityManager() private readonly entityManager: EntityManager) {}

  public async execute({eventId}: FindEventInput) {
    const event = await this.entityManager.getRepository(Event).findOne({
      where: {
        id: eventId,
      },
    });

    return event;
  }
}
