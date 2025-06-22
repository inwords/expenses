package com.inwords.expenses.feature.expenses.domain.model

import com.inwords.expenses.feature.events.domain.model.Person
import com.ionspin.kotlin.bignum.decimal.BigDecimal

data class ExpenseSplitWithPerson(
    val expenseSplitId: Long,
    val expenseId: Long,
    val person: Person,
    val originalAmount: BigDecimal,
    val exchangedAmount: BigDecimal,
)
