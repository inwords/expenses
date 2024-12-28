package com.inwords.expenses.feature.expenses.domain

import com.inwords.expenses.core.utils.divide
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.events.domain.store.local.CurrenciesLocalStore
import com.inwords.expenses.feature.expenses.domain.model.Expense
import com.inwords.expenses.feature.expenses.domain.model.ExpenseSplitWithPerson
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import com.inwords.expenses.feature.expenses.domain.model.ExpensesDetails
import com.inwords.expenses.feature.expenses.domain.model.PersonWithAmount
import com.inwords.expenses.feature.expenses.domain.store.ExpensesLocalStore
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class ExpensesInteractor internal constructor(
    expensesLocalStoreLazy: Lazy<ExpensesLocalStore>,
    currenciesLocalStoreLazy: Lazy<CurrenciesLocalStore>,
    private val currencyExchanger: CurrencyExchanger = CurrencyExchanger(),
) {

    private val _refreshExpenses = MutableSharedFlow<Event>(
        extraBufferCapacity = 2,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val refreshExpenses: Flow<Event> = _refreshExpenses

    private val expensesLocalStore by expensesLocalStoreLazy
    private val currenciesLocalStore by currenciesLocalStoreLazy

    fun getExpensesFlow(eventId: Long): Flow<List<Expense>> {
        return expensesLocalStore.getExpensesFlow(eventId)
    }

    internal fun getExpensesDetails(eventDetails: EventDetails): Flow<ExpensesDetails> {
        return expensesLocalStore.getExpensesFlow(eventDetails.event.id)
            .map { expenses ->
                val debtCalculator = DebtCalculator(expenses, eventDetails.primaryCurrency)

                ExpensesDetails(
                    event = eventDetails,
                    expenses = expenses,
                    debtCalculator = debtCalculator,
                )
            }
    }

    internal suspend fun addExpenseEqualSplit(
        event: Event,
        wholeAmount: BigDecimal,
        expenseType: ExpenseType,
        description: String,
        selectedSubjectPersons: List<Person>,
        selectedCurrency: Currency,
        selectedPerson: Person,
    ) {
        val exchanger = exchanger(event, selectedCurrency) ?: return

        val originalAmount = wholeAmount.divide(
            other = selectedSubjectPersons.size.coerceAtLeast(1).toBigDecimal(),
            scale = 3,
        )
        val subjectExpenseSplitWithPersons = selectedSubjectPersons.map { person ->
            ExpenseSplitWithPerson(
                expenseSplitId = 0,
                expenseId = 0,
                person = person,
                originalAmount = originalAmount,
                exchangedAmount = exchanger.invoke(originalAmount)
            )
        }

        val expense = Expense(
            expenseId = 0,
            serverId = 0,
            currency = selectedCurrency,
            expenseType = expenseType,
            person = selectedPerson,
            subjectExpenseSplitWithPersons = subjectExpenseSplitWithPersons,
            timestamp = Clock.System.now(),
            description = description.trim().ifEmpty { "Без описания" },
        )

        expensesLocalStore.upsert(event, expense)
    }

    internal suspend fun addExpenseCustomSplit(
        event: Event,
        expenseType: ExpenseType,
        description: String,
        selectedCurrency: Currency,
        selectedPerson: Person,
        personWithAmountSplit: List<PersonWithAmount>,
    ) {
        val exchanger = exchanger(event, selectedCurrency) ?: return

        val subjectExpenseSplitWithPersons = personWithAmountSplit.map { personWithAmount ->
            ExpenseSplitWithPerson(
                expenseSplitId = 0,
                expenseId = 0,
                person = personWithAmount.person,
                originalAmount = personWithAmount.amount,
                exchangedAmount = exchanger.invoke(personWithAmount.amount),
            )
        }

        val expense = Expense(
            expenseId = 0,
            serverId = 0,
            currency = selectedCurrency,
            expenseType = expenseType,
            person = selectedPerson,
            subjectExpenseSplitWithPersons = subjectExpenseSplitWithPersons,
            timestamp = Clock.System.now(),
            description = description.trim().ifEmpty { "Без описания" },
        )

        expensesLocalStore.upsert(event, expense)
    }

    internal suspend fun onRefreshExpensesAsync(event: Event) {
        _refreshExpenses.emit(event)
    }

    private fun exchanger(event: Event, originalCurrency: Currency): ((BigDecimal) -> BigDecimal)? {
        if (originalCurrency.id == event.primaryCurrencyId) {
            return { it }
        }

        val primaryCurrencyCode = currenciesLocalStore.getCurrencyCodeById(event.primaryCurrencyId) ?: run {
            // FIXME: non-fatal error
            return null
        }
        return { currencyExchanger.exchange(it, originalCurrency.code, primaryCurrencyCode) }
    }

}