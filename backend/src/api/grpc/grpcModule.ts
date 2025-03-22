import {Module} from '@nestjs/common';
import {UserController} from '#api/grpc/user/user.controller';
import {ExpenseInteractionModule} from '#interaction/expense/expense.interaction.module';
import {CurrencyInteractionModule} from '#interaction/currency/currency.interaction.module';
import {EventInteractionModule} from '#interaction/event/event.interaction.module';
import {UserInteractionModule} from '#interaction/user/user.interaction.module';
import {CurrencyPersistenceModule} from '#persistence/currency/currency.persistence.module';

@Module({
  imports: [
    ExpenseInteractionModule,
    CurrencyInteractionModule,
    EventInteractionModule,
    UserInteractionModule,
    CurrencyPersistenceModule,
  ],
  controllers: [UserController],
})
export class GrpcModule {}
