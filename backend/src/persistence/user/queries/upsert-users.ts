import {IUpsertUsers, UpsertUsersInput} from '../types';
import {InjectEntityManager} from '@nestjs/typeorm';
import {EntityManager} from 'typeorm';
import {User} from '#persistence/entities/user.entity';

export class UpsertUsers implements IUpsertUsers {
  constructor(@InjectEntityManager() private readonly entityManager: EntityManager) {}

  public async execute({users, entityManager}: UpsertUsersInput) {
    return (entityManager || this.entityManager).getRepository(User).save(users);
  }
}
