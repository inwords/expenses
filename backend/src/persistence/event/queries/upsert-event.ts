import {IUpsertEvent, UpsertEventInput} from '../types';
import {InjectEntityManager} from '@nestjs/typeorm';
import {EntityManager} from 'typeorm';
import {Event} from '#persistence/entities/event.entity';
import {Injectable} from "@nestjs/common";

@Injectable()
export class UpsertEvent implements IUpsertEvent {
  constructor(@InjectEntityManager() private readonly entityManager: EntityManager) {}

  public async execute(event: UpsertEventInput) {
    return await this.entityManager.getRepository(Event).save(event);
  }
}
