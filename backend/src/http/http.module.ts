import {Module} from '@nestjs/common';
import {CurrencyModule} from '../currency/currency.module';
import {UserController} from './user/user.controller';
import {EventModule} from '../event/event.module';
import {ExpenseInteractionModule} from '../interaction/expense/expense.interaction.module';

@Module({
  imports: [CurrencyModule, EventModule, ExpenseInteractionModule],
  controllers: [UserController],
})
export class HttpModule {}
