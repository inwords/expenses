import {UseCase} from '#packages/use-case';

import {IEvent} from '#domain/entities/event.entity';
import {RelationalDataServiceAbstract} from '#domain/abstracts/relational-data-service/relational-data-service';
import {IUserInfo} from '#domain/entities/user-info.entity';
import {Injectable} from '@nestjs/common';
import {EventServiceAbstract} from '#domain/abstracts/event-service/event-service';
import {Result, success, isError} from '#packages/result';
import {EventNotFoundError, EventDeletedError, InvalidPinCodeError} from '#domain/errors/errors';

type Input = {eventId: string; pinCode: string};
type Output = Result<IEvent & {users: Array<IUserInfo>}, EventNotFoundError | EventDeletedError | InvalidPinCodeError>;

@Injectable()
export class GetEventInfoUseCase implements UseCase<Input, Output> {
  constructor(
    private readonly rDataService: RelationalDataServiceAbstract,
    private readonly eventService: EventServiceAbstract,
  ) {}

  public async execute({eventId, pinCode}: Input): Promise<Output> {
    const [event] = await this.rDataService.event.findById(eventId);

    const validationResult = this.eventService.isValidEvent(event, pinCode);
    if (isError(validationResult)) {
      return validationResult;
    }

    const [users] = await this.rDataService.userInfo.findByEventId(eventId);

    return success({...event, users});
  }
}
