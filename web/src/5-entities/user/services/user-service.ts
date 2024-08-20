import {addUsersToEvent, deleteUserFromEvent} from '@/5-entities/user/services/api';
import {User} from '@/5-entities/user/types/types';
import {eventStore} from '@/5-entities/event/stores/event-store';
import {userStore} from '@/5-entities/user/stores/user-store';

export class UserService {
  public async addUsersToEvent(users: Omit<User, 'id'>) {
    const currentEvent = eventStore.currentEvent;

    if (currentEvent) {
      const resp = await addUsersToEvent(currentEvent.id, users, currentEvent.pinCode);

      userStore.setUsers([...userStore.users, ...resp]);
    }
  }

  public async deleteUsersFromEvent(id: number) {
    const currentEvent = eventStore.currentEvent;

    if (currentEvent) {
      await deleteUserFromEvent(currentEvent.id, id, currentEvent.pinCode);

      userStore.setUsers(userStore.users.filter((u) => u.id !== id));
    }
  }
}

export const userService = new UserService();
