package com.inwords.expenses.feature.expenses.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.domain.model.Expense
import com.inwords.expenses.feature.expenses.domain.model.ExpenseSplitWithPerson
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import com.inwords.expenses.feature.expenses.ui.converter.toUiModel
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Clock

@Preview(showBackground = true)
@Composable
private fun ExpensesScreenPreviewSuccessWithCreditors() {
    ExpensesScreen(
        onMenuClick = {},
        onAddExpenseClick = {},
        onDebtsDetailsClick = {},
        onReplenishmentClick = {},
        onJoinEventClick = {},
        onCreateEventClick = {},
        onRefresh = {},
        state = SimpleScreenState.Success(mockExpensesScreenUiModel(withCreditors = true))
    )
}

@Preview(showBackground = true)
@Composable
private fun ExpensesScreenPreviewSuccessWithoutCreditors() {
    ExpensesScreen(
        onMenuClick = {},
        onAddExpenseClick = {},
        onDebtsDetailsClick = {},
        onReplenishmentClick = {},
        onJoinEventClick = {},
        onCreateEventClick = {},
        onRefresh = {},
        state = SimpleScreenState.Success(mockExpensesScreenUiModel(withCreditors = false))
    )
}

@Composable
@Preview(showBackground = true)
private fun ExpensesScreenPreviewEmpty() {
    ExpensesScreen(
        onMenuClick = {},
        onAddExpenseClick = {},
        onDebtsDetailsClick = {},
        onReplenishmentClick = {},
        onJoinEventClick = {},
        onCreateEventClick = {},
        onRefresh = {},
        state = SimpleScreenState.Empty
    )
}

internal fun mockExpensesScreenUiModel(withCreditors: Boolean): ExpensesScreenUiModel {
    val person1 = Person(
        id = 1,
        serverId = 11,
        name = "Василий"
    )
    val person2 = Person(
        id = 2,
        serverId = 12,
        name = "Максим"
    )
    return ExpensesScreenUiModel(
        eventName = "France trip",
        currentPersonId = person1.id,
        currentPersonName = person1.name,
        creditors = persistentListOf(
            ExpensesScreenUiModel.DebtorShortUiModel(
                personId = person1.id,
                personName = person1.name,
                currencyCode = "EUR",
                currencyName = "Euro",
                amount = "100"
            ),
            ExpensesScreenUiModel.DebtorShortUiModel(
                personId = person2.id,
                personName = person2.name,
                currencyCode = "EUR",
                currencyName = "Euro",
                amount = "150"
            )
        ).takeIf { withCreditors } ?: persistentListOf(),
        expenses = persistentListOf(
            Expense(
                expenseId = 1,
                serverId = 11,
                currency = Currency(
                    id = 1,
                    serverId = 11,
                    code = "RUB",
                    name = "Russian Ruble",
                ),
                expenseType = ExpenseType.Spending,
                person = person1,
                subjectExpenseSplitWithPersons = listOf(
                    ExpenseSplitWithPerson(
                        expenseSplitId = 1,
                        expenseId = 1,
                        person = person1,
                        originalAmount = 100.toBigDecimal(),
                        exchangedAmount = 100.toBigDecimal(),
                    ),
                    ExpenseSplitWithPerson(
                        expenseSplitId = 2,
                        expenseId = 1,
                        person = person2,
                        originalAmount = 150.333.toBigDecimal(),
                        exchangedAmount = 100.toBigDecimal(),
                    )
                ),
                timestamp = Clock.System.now(),
                description = "Lunch",
            ).toUiModel("EUR"),
            Expense(
                expenseId = 2,
                serverId = 12,
                currency = Currency(
                    id = 2,
                    serverId = 11,
                    code = "USD",
                    name = "US Dollar",
                ),
                expenseType = ExpenseType.Replenishment,
                person = person2,
                subjectExpenseSplitWithPersons = listOf(
                    ExpenseSplitWithPerson(
                        expenseSplitId = 4,
                        expenseId = 2,
                        person = person2,
                        originalAmount = 132423423.toBigDecimal(),
                        exchangedAmount = 132423423.toBigDecimal(),
                    )
                ),
                timestamp = Clock.System.now(),
                description = "Dinner and some text",
            ).toUiModel("EUR")
        ),
        isRefreshing = false
    )
}
