package com.inwords.expenses.feature.expenses.domain.model

import com.inwords.expenses.feature.events.domain.model.Person
import com.ionspin.kotlin.bignum.decimal.BigDecimal

internal data class PersonWithAmount(
    val person: Person,
    val amount: BigDecimal,
)
