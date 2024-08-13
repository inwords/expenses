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
private fun ExpensesScreenPreviewSuccess() {
    ExpensesScreen(
        onAddExpenseClick = {},
        onDebtsDetailsClick = {},
        onReplenishmentClick = {},
        onJoinEventClick = {},
        onCreateEventClick = {},
        state = SimpleScreenState.Success(mockExpensesScreenUiModel())
    )
}

@Composable
@Preview(showBackground = true)
private fun ExpensesScreenPreviewEmpty() {
    ExpensesScreen(
        onAddExpenseClick = {},
        onDebtsDetailsClick = {},
        onReplenishmentClick = {},
        onJoinEventClick = {},
        onCreateEventClick = {},
        state = SimpleScreenState.Empty
    )
}

internal fun mockExpensesScreenUiModel(): ExpensesScreenUiModel {
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
        ),
        expenses = persistentListOf(
            Expense(
                expenseId = 1,
                currency = Currency(
                    id = 1,
                    serverId = 11,
                    code = "RUB",
                    name = "Russian Ruble",
                ),
                expenseType = ExpenseType.Spending,
                person = person1,
                subjecExpenseSplitWithPersons = listOf(
                    ExpenseSplitWithPerson(
                        expenseSplitId = 1,
                        expenseId = 1,
                        person = person1,
                        amount = 100.toBigDecimal()
                    ),
                    ExpenseSplitWithPerson(
                        expenseSplitId = 2,
                        expenseId = 1,
                        person = person2,
                        amount = 150.333.toBigDecimal()
                    )
                ),
                timestamp = Clock.System.now(),
                description = "Lunch",
            ).toUiModel(),
            Expense(
                expenseId = 2,
                currency = Currency(
                    id = 2,
                    serverId = 11,
                    code = "USD",
                    name = "US Dollar",
                ),
                expenseType = ExpenseType.Replenishment,
                person = person2,
                subjecExpenseSplitWithPersons = listOf(
                    ExpenseSplitWithPerson(
                        expenseSplitId = 4,
                        expenseId = 2,
                        person = person2,
                        amount = 132423423.toBigDecimal()
                    )
                ),
                timestamp = Clock.System.now(),
                description = "Dinner and some text",
            ).toUiModel()
        )
    )
}
