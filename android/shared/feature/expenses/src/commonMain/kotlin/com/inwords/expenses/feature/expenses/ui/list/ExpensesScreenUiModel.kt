package com.inwords.expenses.feature.expenses.ui.list

import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import kotlinx.collections.immutable.ImmutableList

internal data class ExpensesScreenUiModel(
    val currentPersonId : Long,
    val currentPersonName: String,
    val creditors: ImmutableList<DebtorShortUiModel>,
    val expenses: ImmutableList<ExpenseUiModel>,
) {

    data class DebtorShortUiModel(
        val personId: Long,
        val personName: String,
        val currencyCode: String,
        val currencyName: String,
        val amount: String,
    )

    data class ExpenseUiModel(
        val expenseId: Long,
        val currencyName: String,
        val expenseType: ExpenseType,
        val personName: String,
        val totalAmount: String,
        val timestamp: String,
        val description: String,
    )

}