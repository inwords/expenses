import {getEventInfo as getEventInfoApi} from '@/entities/event/services/api';
import {userStore} from '@/entities/user/stores/user-store';

export class EventService {
  async getEventInfo(eventId: string, queryParams: Record<string, string>) {
    const resp = await getEventInfoApi(eventId, queryParams);

    userStore.setUsers(resp.users);
  }
}

export const eventService = new EventService();
