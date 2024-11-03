import {EntityManager} from 'typeorm';
import {Query} from '#packages/query';
import {User} from '#domain/user/types';

export type UpsertUsersInput = {users: Array<{name: string; eventId: number}>; entityManager?: EntityManager};
export type IUpsertUsers = Query<UpsertUsersInput, Array<User>>;
export type FindUsersInput = {eventId: number};
export type IFindUsers = Query<FindUsersInput, Array<User>>;
