package com.inwords.expenses.feature.events.data.network

import com.inwords.expenses.core.network.HostConfig
import com.inwords.expenses.core.network.NetworkResult
import com.inwords.expenses.core.network.requestWithExceptionHandling
import com.inwords.expenses.core.network.toBasicResult
import com.inwords.expenses.core.network.url
import com.inwords.expenses.core.utils.Result
import com.inwords.expenses.core.utils.SuspendLazy
import com.inwords.expenses.feature.events.data.network.dto.AddUsersDto
import com.inwords.expenses.feature.events.data.network.dto.CreateEventRequest
import com.inwords.expenses.feature.events.data.network.dto.CreateUserDto
import com.inwords.expenses.feature.events.data.network.dto.EventDto
import com.inwords.expenses.feature.events.data.network.dto.UserDto
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.events.domain.store.local.CurrenciesLocalStore
import com.inwords.expenses.feature.events.domain.store.remote.EventsRemoteStore
import com.inwords.expenses.feature.events.domain.store.remote.EventsRemoteStore.GetEventResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.first

internal class EventsRemoteStoreImpl(
    private val client: SuspendLazy<HttpClient>,
    private val hostConfig: HostConfig,
    private val currenciesLocalStore: CurrenciesLocalStore,
) : EventsRemoteStore {

    override suspend fun getEvent(eventServerId: Long, pinCode: String): GetEventResult {
        val currencies = currenciesLocalStore.getCurrencies().first()

        val result = client.requestWithExceptionHandling {
            get {
                url(hostConfig) {
                    pathSegments = listOf("event", eventServerId.toString())
                    parameters.append("pinCode", pinCode)
                }
            }.body<EventDto>().toEventDetails(
                localEventId = 0L,
                localPersons = emptyList(),
                currencies = currencies
            )
        }
        return when (result) {
            is NetworkResult.Ok -> GetEventResult.Event(result.data)

            is NetworkResult.Error.Http.Client -> when (result.exception.response.status) {
                HttpStatusCode.NotFound -> GetEventResult.EventNotFound
                HttpStatusCode.Forbidden -> GetEventResult.InvalidAccessCode
                else -> GetEventResult.OtherError
            }

            is NetworkResult.Error -> GetEventResult.OtherError
        }
    }

    override suspend fun createEvent(
        event: Event,
        currencies: List<Currency>,
        primaryCurrencyId: Long,
        localPersons: List<Person>,
    ): Result<EventDetails> {
        return client.requestWithExceptionHandling {
            post {
                url(hostConfig) { pathSegments = listOf("event") }
                contentType(ContentType.Application.Json)
                setBody(
                    CreateEventRequest(
                        name = event.name,
                        currencyId = primaryCurrencyId,
                        users = localPersons.map { it.toCreateUserDto() },
                        pinCode = event.pinCode,
                    )
                )
            }.body<EventDto>().toEventDetails(event.id, localPersons, currencies)
        }.toBasicResult()
    }

    // FIXME check pinCode on backend
    override suspend fun addPersonsToEvent(
        eventServerId: Long,
        pinCode: String,
        localPersons: List<Person>
    ): Result<List<Person>> {
        return client.requestWithExceptionHandling {
            post {
                url(hostConfig) { pathSegments = listOf("event", eventServerId.toString(), "users") }
                contentType(ContentType.Application.Json)
                setBody(
                    AddUsersDto(users = localPersons.map { it.toCreateUserDto() })
                )
            }.body<List<UserDto>>().mapIndexed { i, dto ->
                dto.toPerson(localPersonId = localPersons[i].id)
            }
        }.toBasicResult()
    }

    private fun EventDto.toEventDetails(localEventId: Long, localPersons: List<Person>?, currencies: List<Currency>): EventDetails {
        return EventDetails(
            event = Event(id = localEventId, serverId = id, name = name, pinCode = pinCode),
            currencies = currencies,
            persons = users.mapIndexed { i, dto ->
                dto.toPerson(localPersonId = localPersons?.get(i)?.id)
            },
            primaryCurrency = currencies.first { it.id == currencyId },
        )
    }

    private fun Person.toCreateUserDto(): CreateUserDto {
        return CreateUserDto(name = name)
    }

    private fun UserDto.toPerson(localPersonId: Long?): Person {
        return Person(id = localPersonId ?: 0L, serverId = id, name = name)
    }

}
