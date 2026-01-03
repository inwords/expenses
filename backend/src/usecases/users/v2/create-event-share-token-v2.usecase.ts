import {UseCase} from '#packages/use-case';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {EventServiceAbstract} from '#domain/abstracts/event-service/event-service';
import {Injectable} from '@nestjs/common';
import {EventShareTokenValueObject} from '#domain/value-objects/event-share-token.value-object';

type Input = {eventId: string; pinCode: string};
type Output = {token: string; expiresAt: Date};

@Injectable()
export class CreateEventShareTokenV2UseCase implements UseCase<Input, Output> {
  constructor(
    private readonly rDataService: RelationalDataServiceAbstract,
    private readonly eventService: EventServiceAbstract,
  ) {}

  public async execute({eventId, pinCode}: Input): Promise<Output> {
    const [event] = await this.rDataService.event.findById(eventId);

    this.eventService.validateEvent(event, pinCode);

    const [existingToken] = await this.rDataService.eventShareToken.findOneActiveByEventId(eventId);

    if (existingToken) {
      return {token: existingToken.token, expiresAt: existingToken.expiresAt};
    }

    const shareToken = new EventShareTokenValueObject({eventId});

    await this.rDataService.eventShareToken.insert(shareToken.value);

    return {token: shareToken.value.token, expiresAt: shareToken.value.expiresAt};
  }
}
