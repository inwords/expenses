package com.inwords.expenses.feature.expenses.data.db.converter

import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.expenses.data.db.entity.ExpenseEntity
import com.inwords.expenses.feature.expenses.domain.model.Expense

internal fun Expense.toEntity(event: Event): ExpenseEntity {
    return ExpenseEntity(
        eventId = event.id,
        amountUnscaled = this.amount.unscaledValue(),
        amountScale = this.amount.scale(),
        currencyId = this.currency.id,
        expenseType = this.expenseType,
        personId = this.person.id,
        timestamp = this.timestamp,
        description = this.description,
    )
}