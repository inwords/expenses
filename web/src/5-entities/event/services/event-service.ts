import {getEventInfo as getEventInfoApi} from '@/5-entities/event/services/api';
import {userStore} from '@/5-entities/user/stores/user-store';

export class EventService {
  async getEventInfo(eventId: string, queryParams: Record<string, string>) {
    const resp = await getEventInfoApi(eventId, queryParams);

    userStore.setUsers(resp.users);
  }
}

export const eventService = new EventService();
