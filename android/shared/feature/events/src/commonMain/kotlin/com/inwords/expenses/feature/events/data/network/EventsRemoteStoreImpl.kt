package com.inwords.expenses.feature.events.data.network

import com.inwords.expenses.core.network.HostConfig
import com.inwords.expenses.core.network.requestWithExceptionHandling
import com.inwords.expenses.core.network.toBasicResult
import com.inwords.expenses.core.network.url
import com.inwords.expenses.core.utils.Result
import com.inwords.expenses.core.utils.SuspendLazy
import com.inwords.expenses.feature.events.data.network.dto.CreateEventRequest
import com.inwords.expenses.feature.events.data.network.dto.CreateUserDto
import com.inwords.expenses.feature.events.data.network.dto.EventDto
import com.inwords.expenses.feature.events.data.network.dto.UserDto
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.events.domain.store.remote.CurrenciesRemoteStore
import com.inwords.expenses.feature.events.domain.store.remote.EventsRemoteStore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class EventsRemoteStoreImpl(
    private val client: SuspendLazy<HttpClient>,
    private val hostConfig: HostConfig,
    private val currenciesRemoteStore: CurrenciesRemoteStore,
) : EventsRemoteStore {

    override suspend fun getEvent(eventId: Long, pinCode: String): Result<EventDetails> {
        val currencies = when (val currenciesResult = currenciesRemoteStore.getCurrencies()) { // FIXME: remove costyl
            is Result.Success -> currenciesResult.data
            is Result.Error -> return Result.Error
        }

        return client.requestWithExceptionHandling {
            get {
                url(hostConfig) {
                    pathSegments = listOf("event", eventId.toString())
                    parameters.append("pinCode", pinCode)
                }
            }.body<EventDto>().toEventDetails(currencies)
        }.toBasicResult()
    }

    override suspend fun createEvent(
        name: String,
        pinCode: String,
        currencies: List<Currency>,
        primaryCurrencyId: Long,
        users: List<Person>,
    ): Result<EventDetails> {
        return client.requestWithExceptionHandling {
            post {
                url(hostConfig) { pathSegments = listOf("event") }
                contentType(ContentType.Application.Json)
                setBody(
                    CreateEventRequest(
                        name = name,
                        currencyId = primaryCurrencyId,
                        users = users.map { it.toUserDto() },
                        pinCode = pinCode,
                    )
                )
            }.body<EventDto>().toEventDetails(currencies)
        }.toBasicResult()
    }

    // FIXME: implement addUsersToEvent

    private fun EventDto.toEventDetails(currencies: List<Currency>): EventDetails {
        return EventDetails(
            event = Event(id = 0L, serverId = id, name = name, pinCode = pinCode),
            currencies = currencies,
            persons = users.map { it.toPerson() },
            primaryCurrency = currencies.first { it.id == currencyId },
        )
    }

    private fun Person.toUserDto(): CreateUserDto {
        return CreateUserDto(name = name)
    }

    private fun UserDto.toPerson(): Person {
        return Person(id = 0L, serverId = id, name = name)
    }

}
