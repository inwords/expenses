package com.inwords.expenses.feature.expenses.data.db.converter

import com.inwords.expenses.feature.events.data.db.entity.CurrencyEntity
import com.inwords.expenses.feature.events.data.db.entity.PersonEntity
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.data.db.entity.ExpenseWithDetailsQuery
import com.inwords.expenses.feature.expenses.domain.model.Expense
import java.math.BigDecimal

internal fun ExpenseWithDetailsQuery.toDomain(): Expense {
    return Expense(
        expenseId = this.expense.expenseId,
        amount = BigDecimal(this.expense.amountUnscaled, this.expense.amountScale),
        currency = this.currency.toDomain(),
        expenseType = this.expense.expenseType,
        person = this.person.toDomain(),
        subjectPersons = this.subjectPersons.map { it.toDomain() },
        timestamp = this.expense.timestamp,
        description = this.expense.description,
    )
}

internal fun CurrencyEntity.toDomain(): Currency {
    return Currency(
        id = this.currencyId,
        code = this.code,
        name = this.name,
    )
}

internal fun PersonEntity.toDomain(): Person {
    return Person(
        id = this.personId,
        name = this.name,
    )
}