import {Injectable} from '@nestjs/common';
import {InjectEntityManager} from '@nestjs/typeorm';
import {EntityManager} from 'typeorm';
import {User} from './user.entity';

@Injectable()
export class UserService {
  constructor(@InjectEntityManager() private readonly entityManager: EntityManager) {}

  public async saveUsers(users: Array<{name: string; eventId: number}>, entityManager?: EntityManager) {
    return (entityManager || this.entityManager).getRepository(User).save(users);
  }
}
