import {createEvent, getEventInfo as getEventInfoApi} from '@/5-entities/event/services/api';
import {userStore} from '@/5-entities/user/stores/user-store';
import {CreateEvent} from '@/5-entities/event/types/types';
import {eventStore} from "@/5-entities/event/stores/event-store";

export class EventService {
  async getEventInfo(eventId: string, queryParams: Record<string, string>) {
    const resp = await getEventInfoApi(eventId, queryParams);

    userStore.setUsers(resp.users);
    eventStore.setCurrentEventName(resp.name);
  }

  async createEvent(data: CreateEvent) {
    const resp = await createEvent(data);

    userStore.setUsers(resp.users);
    eventStore.setCurrentEventName(resp.name);

    return resp.id;
  }
}

export const eventService = new EventService();
