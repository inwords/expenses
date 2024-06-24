package com.inwords.expenses.feature.expenses.domain.model

import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.expenses.domain.DebtCalculator

internal data class ExpensesDetails(
    val event: EventDetails,
    val expenses: List<Expense>,
    val debtCalculator: DebtCalculator,
)