package com.inwords.expenses.feature.expenses.ui.list

import com.inwords.expenses.feature.expenses.domain.model.Expense

internal data class ExpensesScreenUiModel(
    val expenses: List<ExpenseUiModel>,
) {

    data class ExpenseUiModel(
        val expense: Expense,
    )
}