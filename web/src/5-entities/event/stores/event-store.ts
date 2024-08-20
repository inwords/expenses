import {makeAutoObservable} from 'mobx';
import {Event} from "@/5-entities/event/types/types";

export class EventStore {
  currentEvent?: Event = undefined;

  constructor() {
    makeAutoObservable(this);
  }

  setCurrentEvent(event: Event) {
    this.currentEvent = event;
  }
}

export const eventStore = new EventStore();
