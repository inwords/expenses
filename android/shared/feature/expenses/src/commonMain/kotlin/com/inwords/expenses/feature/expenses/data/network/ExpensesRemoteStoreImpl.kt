package com.inwords.expenses.feature.expenses.data.network

import com.inwords.expenses.core.network.HostConfig
import com.inwords.expenses.core.network.requestWithExceptionHandling
import com.inwords.expenses.core.network.toIoResult
import com.inwords.expenses.core.network.url
import com.inwords.expenses.core.utils.IoResult
import com.inwords.expenses.core.utils.SuspendLazy
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.data.network.dto.CreateExpenseRequest
import com.inwords.expenses.feature.expenses.data.network.dto.ExpenseDto
import com.inwords.expenses.feature.expenses.data.network.dto.SplitInformationDto
import com.inwords.expenses.feature.expenses.data.network.dto.SplitInformationRequest
import com.inwords.expenses.feature.expenses.domain.model.Expense
import com.inwords.expenses.feature.expenses.domain.model.ExpenseSplitWithPerson
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import com.inwords.expenses.feature.expenses.domain.store.ExpensesRemoteStore
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

internal class ExpensesRemoteStoreImpl(
    private val client: SuspendLazy<HttpClient>,
    private val hostConfig: HostConfig,
) : ExpensesRemoteStore {

    override suspend fun getExpenses(
        event: Event,
        currencies: List<Currency>,
        persons: List<Person>
    ): IoResult<List<Expense>> {
        return client.requestWithExceptionHandling {
            get {
                url(hostConfig) { pathSegments = listOf("api", "user", "event", event.serverId.toString(), "expenses") }
            }.body<List<ExpenseDto>>().mapNotNull { it.toExpense(localExpense = null, currencies, persons) }
        }.toIoResult()
    }

    override suspend fun addExpensesToEvent(
        event: Event,
        expenses: List<Expense>,
        currencies: List<Currency>,
        persons: List<Person>
    ): List<IoResult<Expense>> = coroutineScope {
        expenses.map { expense ->
            async { addExpenseToEvent(event, expense, currencies, persons) }
        }.map { it.await() }
    }

    private suspend fun addExpenseToEvent(
        event: Event,
        expense: Expense,
        currencies: List<Currency>,
        persons: List<Person>
    ): IoResult<Expense> {
        return client.requestWithExceptionHandling {
            post {
                url(hostConfig) { pathSegments = listOf("api", "user", "event", event.serverId.toString(), "expense") }
                contentType(ContentType.Application.Json)
                setBody(
                    CreateExpenseRequest(
                        currencyId = expense.currency.serverId,
                        expenseType = when (expense.expenseType) {
                            ExpenseType.Spending -> "expense"
                            ExpenseType.Replenishment -> "refund"
                        },
                        userWhoPaidId = expense.person.serverId,
                        splitInformation = expense.subjectExpenseSplitWithPersons.map { expenseSplitWithPerson ->
                            SplitInformationRequest(
                                userId = expenseSplitWithPerson.person.serverId,
                                amount = expenseSplitWithPerson.originalAmount.doubleValue(false),
                            )
                        },
                        description = expense.description
                    )
                )
            }.body<ExpenseDto>().toExpense(expense, currencies, persons)
        }.toIoResult()
    }

    private fun ExpenseDto.toExpense(
        localExpense: Expense?,
        currencies: List<Currency>,
        persons: List<Person>
    ): Expense? {
        return Expense(
            expenseId = localExpense?.expenseId ?: 0L,
            serverId = id,
            currency = currencies.firstOrNull { it.serverId == currencyId } ?: return null, // FIXME non-fatal error
            expenseType = when (expenseType) {
                "expense" -> ExpenseType.Spending
                "refund" -> ExpenseType.Replenishment
                else -> return null
            },
            person = persons.firstOrNull { it.serverId == userWhoPaidId } ?: return null,
            subjectExpenseSplitWithPersons = splitInformation.map { it.toDomain(persons) ?: return null },
            timestamp = createdAt,
            description = description,
        )
    }

    private fun SplitInformationDto.toDomain(persons: List<Person>): ExpenseSplitWithPerson? {
        val person = persons.firstOrNull { it.serverId == userId } ?: return null
        val originalAmount = BigDecimal.fromDouble(amount)
        val exchangedAmount = BigDecimal.fromDouble(exchangedAmount)
        return ExpenseSplitWithPerson(
            expenseSplitId = 0L,
            expenseId = 0L,
            person = Person(
                id = person.id,
                serverId = userId,
                name = person.name,
            ),
            originalAmount = originalAmount,
            exchangedAmount = exchangedAmount,
        )
    }

}
