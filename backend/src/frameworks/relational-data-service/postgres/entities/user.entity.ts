import {Column, Entity, PrimaryColumn} from 'typeorm';
import {type IUser} from '#domain/entities/user.enitity';

@Entity('user')
export class UserEntity implements IUser {
  @PrimaryColumn({type: 'varchar'})
  id: IUser['id'];

  @Column({type: 'varchar'})
  name: IUser['name'];

  @Column({type: 'varchar'})
  eventId: IUser['eventId'];

  @Column({type: 'timestamptz'})
  createdAt: IUser['createdAt'];

  @Column({type: 'timestamptz'})
  updatedAt: IUser['updatedAt'];
}
