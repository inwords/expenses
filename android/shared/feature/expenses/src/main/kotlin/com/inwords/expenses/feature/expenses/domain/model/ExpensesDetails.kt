package com.inwords.expenses.feature.expenses.domain.model

import com.inwords.expenses.feature.events.domain.model.EventDetails

data class ExpensesDetails(
    val event: EventDetails,
    val expenses: List<Expense>,
)