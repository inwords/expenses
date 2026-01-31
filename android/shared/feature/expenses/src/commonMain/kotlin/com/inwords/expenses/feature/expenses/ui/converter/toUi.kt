package com.inwords.expenses.feature.expenses.ui.converter

import com.inwords.expenses.core.ui.utils.formatLocalDateTime
import com.inwords.expenses.core.ui.utils.getDefaultDateTimeFormat
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.domain.model.Expense
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import com.inwords.expenses.feature.expenses.ui.debts_list.DebtsListPaneUiModel
import com.inwords.expenses.feature.expenses.ui.list.ExpensesPaneUiModel
import com.inwords.expenses.feature.expenses.ui.utils.toRoundedString

internal fun Expense.toUiModel(primaryCurrencyName: String): ExpensesPaneUiModel.Expenses.ExpenseUiModel {
    val amountSign = when (expenseType) {
        ExpenseType.Spending -> "-"
        ExpenseType.Replenishment -> "+"
    }
    return ExpensesPaneUiModel.Expenses.ExpenseUiModel(
        expenseId = expenseId,
        currencyText = if (currency.name == primaryCurrencyName) {
            currency.name
        } else {
            "$primaryCurrencyName (${currency.name})"
        },
        expenseType = expenseType,
        personName = person.name,
        totalAmount = "$amountSign${totalAmount.toRoundedString()}",
        timestamp = timestamp.formatLocalDateTime(getDefaultDateTimeFormat()),
        description = description
    )
}

internal fun Person.toUiModel(): DebtsListPaneUiModel.PersonUiModel {
    return DebtsListPaneUiModel.PersonUiModel(
        personId = id,
        personName = name
    )
}