import {makeAutoObservable} from "mobx";

export class EventStore {
    currentEventName?: string = undefined;

    constructor() {
        makeAutoObservable(this);
    }

    setCurrentEventName(eventName: string) {
        this.currentEventName = eventName;
    }
}

export const eventStore = new EventStore();