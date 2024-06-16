package com.inwords.expenses.feature.expenses.ui.list

import androidx.compose.runtime.Immutable
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.domain.model.Expense
import java.math.BigDecimal

@Immutable
internal data class ExpensesScreenUiModel(
    val creditors: List<DebtorShortUiModel>,
    val expenses: List<ExpenseUiModel>,
) {

    @Immutable
    data class DebtorShortUiModel(
        val person: Person,
        val amount: BigDecimal,
    )

    @Immutable
    data class ExpenseUiModel(
        val expense: Expense,
    )
}