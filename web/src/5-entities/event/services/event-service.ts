import {
  createEvent,
  getEventInfo as getEventInfoApi,
  createEventShareToken as createEventShareTokenApi,
} from '@/5-entities/event/services/api';
import {userStore} from '@/5-entities/user/stores/user-store';
import {CreateEvent} from '@/5-entities/event/types/types';
import {eventStore} from '@/5-entities/event/stores/event-store';

export class EventService {
  async getEventInfo(eventId: string, params: {pinCode?: string; token?: string}) {
    const resp = await getEventInfoApi(eventId, params);

    userStore.setUsers(resp.users);
    eventStore.setCurrentEvent(resp);
  }

  async createEvent(data: CreateEvent) {
    const resp = await createEvent(data);

    userStore.setUsers(resp.users);
    eventStore.setCurrentEvent(resp);

    return resp.id;
  }

  async createEventShareToken(eventId: string, pinCode: string) {
    return await createEventShareTokenApi(eventId, pinCode);
  }
}

export const eventService = new EventService();
