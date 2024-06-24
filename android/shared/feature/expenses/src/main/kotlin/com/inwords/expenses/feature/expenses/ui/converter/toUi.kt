package com.inwords.expenses.feature.expenses.ui.converter

import com.inwords.expenses.core.ui.utils.defaultDateFormat
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.domain.model.Expense
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import com.inwords.expenses.feature.expenses.ui.debts_list.DebtsListScreenUiModel
import com.inwords.expenses.feature.expenses.ui.list.ExpensesScreenUiModel
import com.inwords.expenses.feature.expenses.ui.utils.toRoundedString
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime

internal fun Expense.toUiModel(): ExpensesScreenUiModel.ExpenseUiModel {
    val amountSign = when (expenseType) {
        ExpenseType.Spending -> "-"
        ExpenseType.Replenishment -> "+"
    }
    return ExpensesScreenUiModel.ExpenseUiModel(
        expenseId = expenseId,
        currencyName = currency.name,
        expenseType = expenseType,
        personName = person.name,
        totalAmount = "$amountSign${totalAmount.toRoundedString()}",
        timestamp = timestamp.toLocalDateTime(TimeZone.currentSystemDefault()).format(defaultDateFormat),
        description = description
    )
}

internal fun Person.toUiModel(): DebtsListScreenUiModel.PersonUiModel {
    return DebtsListScreenUiModel.PersonUiModel(
        personId = id,
        personName = name
    )
}