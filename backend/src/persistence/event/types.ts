import {Query} from '../../packages/query';
import {Event} from '../../domain/event/types';

export type FindEventInput = {eventId: number};
export type IFindEvent = Query<FindEventInput, Event | null>;
