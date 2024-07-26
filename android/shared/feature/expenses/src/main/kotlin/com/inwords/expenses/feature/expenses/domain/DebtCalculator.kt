package com.inwords.expenses.feature.expenses.domain

import androidx.annotation.WorkerThread
import com.inwords.expenses.core.utils.sumOf
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.domain.model.AccumulatedDebt
import com.inwords.expenses.feature.expenses.domain.model.BarterAccumulatedDebt
import com.inwords.expenses.feature.expenses.domain.model.Debt
import com.inwords.expenses.feature.expenses.domain.model.Expense
import com.ionspin.kotlin.bignum.decimal.BigDecimal

internal class DebtCalculator(
    private val expenses: List<Expense>,
    private val primaryCurrency: Currency,
    private val currencyExchanger: CurrencyExchanger = CurrencyExchanger(),
) {

    /**
     * Who owes to whom and how much.
     * Does not include the debts in the opposite direction.
     */
    @get:WorkerThread
    val accumulatedDebts: Map<Person, Map<Person, AccumulatedDebt>> by lazy {
        calculateAccumulatedDebts()
    }

    /**
     * Who owes to whom and how much.
     * Includes the debts in the opposite direction.
     */
    @get:WorkerThread
    val barterAccumulatedDebts: Map<Person, Map<Person, BarterAccumulatedDebt>> by lazy {
        val threshold = BigDecimal.fromDouble(0.01)
        accumulatedDebts.mapValuesTo(HashMap()) { (debtor, creditorToAccumulatedDebts) ->
            creditorToAccumulatedDebts.mapValuesTo(HashMap()) { (creditor, accumulatedDebt) ->
                BarterAccumulatedDebt(
                    debtorToCreditorDebt = accumulatedDebt,
                    creditorToDebtorDebt = accumulatedDebts[creditor]?.get(debtor),
                )
            }.filterValues { it.barterAmount > threshold }
        }.filterValues { it.isNotEmpty() }
    }

    fun getBarterAccumulatedDebtForPerson(person: Person): Map<Person, BarterAccumulatedDebt> {
        return barterAccumulatedDebts[person] ?: emptyMap()
    }

    /**
     * Calculate the accumulated debts for each person.
     * Who owes to whom and how much.
     */
    private fun calculateAccumulatedDebts(): Map<Person, Map<Person, AccumulatedDebt>> {
        val debtorToCreditorDebts = hashMapOf<Person, MutableMap<Person, MutableList<Debt>>>()

        expenses.forEach { expense ->
            expense.subjecExpenseSplitWithPersons.forEach { subjectExpenseSplit ->
                if (subjectExpenseSplit.person != expense.person) {
                    val subjectDebtorToCreditorDebts = debtorToCreditorDebts.getOrPut(subjectExpenseSplit.person) { mutableMapOf() }

                    val debt = Debt(
                        creditor = expense.person,
                        debtor = subjectExpenseSplit.person,
                        amount = subjectExpenseSplit.amount,
                        expense = expense,
                    )

                    subjectDebtorToCreditorDebts.getOrPut(expense.person) { mutableListOf() }.add(debt)
                }
            }
        }

        val debtorToCreditorToAccumulatedDebts = debtorToCreditorDebts.mapValuesTo(HashMap()) { (debtor, creditorToDebts) ->
            creditorToDebts.mapValuesTo(HashMap()) { (creditor, debts) ->
                BigDecimal
                val amount = debts.sumOf {
                    if (it.expense.currency.id == primaryCurrency.id) {
                        it.amount
                    } else {
                        currencyExchanger.exchange(it.amount, it.expense.currency.code, primaryCurrency.code)
                    }
                }
                AccumulatedDebt(
                    creditor = creditor,
                    debtor = debtor,
                    currency = primaryCurrency,
                    amount = amount,
                    debts = debts,
                )
            }
        }

        return debtorToCreditorToAccumulatedDebts
    }

}