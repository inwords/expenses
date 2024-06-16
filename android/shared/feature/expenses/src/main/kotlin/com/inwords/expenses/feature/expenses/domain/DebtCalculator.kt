package com.inwords.expenses.feature.expenses.domain

import androidx.annotation.WorkerThread
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.domain.model.AccumulatedDebt
import com.inwords.expenses.feature.expenses.domain.model.BarterAccumulatedDebt
import com.inwords.expenses.feature.expenses.domain.model.Debt
import com.inwords.expenses.feature.expenses.domain.model.ExpensesDetails

internal class DebtCalculator(
    private val expensesDetails: ExpensesDetails
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
        accumulatedDebts.mapValues { (debtor, creditorToAccumulatedDebts) ->
            creditorToAccumulatedDebts.mapValues { (creditor, accumulatedDebt) ->
                BarterAccumulatedDebt(
                    debtorToCreditorDebt = accumulatedDebt,
                    creditorToDebtorDebt = accumulatedDebts[creditor]?.get(debtor),
                )
            }
        }
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

        expensesDetails.expenses.forEach { expense ->
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

        val debtorToCreditorToAccumulatedDebts = debtorToCreditorDebts.mapValues { (debtor, creditorToDebts) ->
            creditorToDebts.mapValues { (creditor, debts) ->
                val amount = debts.sumOf { it.amount }
                AccumulatedDebt(
                    creditor = creditor,
                    debtor = debtor,
                    amount = amount,
                    debts = debts,
                )
            }
        }

        return debtorToCreditorToAccumulatedDebts
    }

}