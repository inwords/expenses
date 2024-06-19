package com.inwords.expenses.feature.expenses.domain.model

import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Person
import java.math.BigDecimal

internal data class Debt(
    val creditor: Person,
    val debtor: Person,
    val amount: BigDecimal,
    val expense: Expense,
)

internal data class AccumulatedDebt(
    val creditor: Person,
    val debtor: Person,
    val currency: Currency,
    val amount: BigDecimal,
    val debts: List<Debt>,
)

internal data class BarterAccumulatedDebt(
    val debtorToCreditorDebt: AccumulatedDebt,
    val creditorToDebtorDebt: AccumulatedDebt?,
) {
    val barterAmount: BigDecimal = debtorToCreditorDebt.amount
        .minus(creditorToDebtorDebt?.amount ?: BigDecimal.ZERO)
        .coerceAtLeast(BigDecimal.ZERO)

    val currency: Currency = debtorToCreditorDebt.currency
}
