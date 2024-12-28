package com.inwords.expenses.feature.expenses.data.db.converter

import com.inwords.expenses.feature.events.data.db.entity.CurrencyEntity
import com.inwords.expenses.feature.events.data.db.entity.PersonEntity
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.data.db.entity.ExpenseSplitWithPersonQuery
import com.inwords.expenses.feature.expenses.data.db.entity.ExpenseWithDetailsQuery
import com.inwords.expenses.feature.expenses.domain.model.Expense
import com.inwords.expenses.feature.expenses.domain.model.ExpenseSplitWithPerson
import com.ionspin.kotlin.bignum.decimal.BigDecimal

internal fun ExpenseWithDetailsQuery.toDomain(): Expense {
    return Expense(
        expenseId = this.expense.expenseId,
        serverId = this.expense.serverId,
        currency = this.currency.toDomain(),
        expenseType = this.expense.expenseType,
        person = this.person.toDomain(),
        subjectExpenseSplitWithPersons = this.expenseSplitWithPersons.map { it.toDomain() },
        timestamp = this.expense.timestamp,
        description = this.expense.description,
    )
}

internal fun CurrencyEntity.toDomain(): Currency {
    return Currency(
        id = this.currencyId,
        serverId = this.currencyServerId,
        code = this.code,
        name = this.name,
    )
}

internal fun PersonEntity.toDomain(): Person {
    return Person(
        id = this.personId,
        serverId = this.personServerId,
        name = this.name,
    )
}

internal fun ExpenseSplitWithPersonQuery.toDomain(): ExpenseSplitWithPerson {
    val originalAmountUnscaled = this.expenseSplitEntity.originalAmountUnscaled
    val originalAmountScale = this.expenseSplitEntity.originalAmountScale

    return ExpenseSplitWithPerson(
        expenseSplitId = this.expenseSplitEntity.expenseSplitId,
        expenseId = this.expenseSplitEntity.expenseId,
        person = this.person.toDomain(),
        originalAmount = if (originalAmountUnscaled != null && originalAmountScale != null) {
            BigDecimal.fromBigIntegerWithExponent(
                bigInteger = originalAmountUnscaled,
                exponent = originalAmountScale
            )
        } else {
            null
        },
        exchangedAmount = BigDecimal.fromBigIntegerWithExponent(
            bigInteger = this.expenseSplitEntity.exchangedAmountUnscaled,
            exponent = this.expenseSplitEntity.exchangedAmountScale
        ),
    )
}