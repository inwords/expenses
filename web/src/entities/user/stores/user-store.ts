import {User} from '@/entities/user/types/types';
import {action, computed, makeObservable, observable} from 'mobx';

export class UserStore {
  users: Array<User> = [];
  currentUser: User | undefined = undefined;

  constructor() {
    makeObservable(this, {
      users: observable,
      currentUser: observable,
      usersToSelect: computed,
      setUsers: action,
      setCurrentUser: action,
    });
  }

  get usersToSelect() {
    return this.users.map((u) => {
      return {id: u.id, label: u.name}
    })
  }

  setUsers(users: Array<User>) {
    this.users = users;
  }

  setCurrentUser(user: User) {
    this.currentUser = user;
  }
}

export const userStore = new UserStore();
