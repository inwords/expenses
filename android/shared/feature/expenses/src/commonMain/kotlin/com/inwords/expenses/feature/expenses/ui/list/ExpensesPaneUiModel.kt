package com.inwords.expenses.feature.expenses.ui.list

import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import com.inwords.expenses.feature.expenses.ui.common.DebtShortUiModel
import kotlinx.collections.immutable.ImmutableList

internal sealed interface ExpensesPaneUiModel {

    data class Expenses(
        val eventName: String,
        val currentPersonId: Long,
        val currentPersonName: String,
        val debts: ImmutableList<DebtShortUiModel>,
        val expenses: ImmutableList<ExpenseUiModel>,
        val isRefreshing: Boolean,
    ) : ExpensesPaneUiModel {

        data class ExpenseUiModel(
            val expenseId: Long,
            val currencyText: String,
            val expenseType: ExpenseType,
            val personName: String,
            val totalAmount: String,
            val timestamp: String,
            val description: String,
        )
    }

    data class LocalEvents(
        val events: ImmutableList<LocalEventUiModel>
    ) : ExpensesPaneUiModel {

        data class LocalEventUiModel(
            val eventId: Long,
            val eventName: String,
        )
    }
}
