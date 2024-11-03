import {FindUsersInput, IFindUsers} from '../types';
import {InjectEntityManager} from '@nestjs/typeorm';
import {EntityManager} from 'typeorm';
import {User} from '#persistence/entities/user.entity';

export class FindUsers implements IFindUsers {
  constructor(@InjectEntityManager() private readonly entityManager: EntityManager) {}

  public async execute({eventId}: FindUsersInput) {
    return await this.entityManager.getRepository(User).find({
      where: {
        eventId,
      },
    });
  }
}
