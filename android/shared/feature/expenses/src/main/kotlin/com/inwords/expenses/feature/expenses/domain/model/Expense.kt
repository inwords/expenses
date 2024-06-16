package com.inwords.expenses.feature.expenses.domain.model

import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Person
import kotlinx.datetime.Instant
import java.math.BigDecimal

data class Expense(
    val expenseId: Long,
    val currency: Currency,
    val expenseType: ExpenseType,
    val person: Person,
    val subjecExpenseSplitWithPersons: List<ExpenseSplitWithPerson>,
    val timestamp: Instant,
    val description: String,
) {

    val totalAmount: BigDecimal = subjecExpenseSplitWithPersons.sumOf { it.amount }
}
