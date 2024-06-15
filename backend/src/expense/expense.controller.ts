import {Body, Controller, Get, Param, Post} from '@nestjs/common';
import {EXPENSE_ROUTES} from './constants';
import {ExpenseService} from './expense.service';
import {EventIdDto, ExpenseCreatedDto} from './dto/expense';

@Controller(EXPENSE_ROUTES.root)
export class ExpenseController {
  constructor(private readonly expenseService: ExpenseService) {}

  @Get(EXPENSE_ROUTES.getAllEventExpenses)
  async getAllEventExpenses(@Param() {eventId}: EventIdDto) {
    return this.expenseService.getAllEventExpenses(eventId);
  }

  @Post(EXPENSE_ROUTES.createExpense)
  async createExpense(@Body() expense: ExpenseCreatedDto) {
    return this.expenseService.saveExpense(expense);
  }
}
