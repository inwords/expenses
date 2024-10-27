import {Module} from '@nestjs/common';
import {UserController} from './user/user.controller';
import {ExpenseInteractionModule} from '../../interaction/expense/expense.interaction.module';
import {CurrencyModule} from '../../currency/currency.module';
import {EventModule} from '../../event/event.module';

@Module({
  imports: [CurrencyModule, EventModule, ExpenseInteractionModule],
  controllers: [UserController],
})
export class HttpModule {}
