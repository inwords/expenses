package com.inwords.expenses.feature.events.data.network.store

import com.inwords.expenses.core.network.HostConfig
import com.inwords.expenses.core.network.NetworkResult
import com.inwords.expenses.core.network.requestWithExceptionHandling
import com.inwords.expenses.core.network.toIoResult
import com.inwords.expenses.core.network.url
import com.inwords.expenses.core.utils.IoResult
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

internal class EventsRemoteStoreImpl(
    private val client: SuspendLazy<HttpClient>,
    private val hostConfig: HostConfig,
) : EventsRemoteStore {

    override suspend fun getEvent(
        event: Event,
        currencies: List<Currency>,
        localPersons: List<Person>?,
    ): GetEventResult {
        val result = client.requestWithExceptionHandling {
            get {
                url(hostConfig) {
                    pathSegments = listOf("user", "event", event.serverId.toString())
                    parameters.append("pinCode", event.pinCode)
                }
            }.body<EventDto>().toEventDetails(
                localEventId = event.id,
                localPersons = localPersons,
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
    ): IoResult<EventDetails> {
        return client.requestWithExceptionHandling {
            post {
                url(hostConfig) { pathSegments = listOf("user", "event") }
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
        }.toIoResult()
    }

    // FIXME check pinCode on backend
    override suspend fun addPersonsToEvent(
        eventServerId: Long,
        pinCode: String,
        localPersons: List<Person>
    ): IoResult<List<Person>> {
        return client.requestWithExceptionHandling {
            post {
                url(hostConfig) { pathSegments = listOf("user", "event", eventServerId.toString(), "users") }
                contentType(ContentType.Application.Json)
                setBody(
                    AddUsersDto(users = localPersons.map { it.toCreateUserDto() })
                )
            }.body<List<UserDto>>().mapIndexed { i, dto ->
                dto.toPerson(localPersonId = localPersons[i].id)
            }
        }.toIoResult()
    }

    private fun EventDto.toEventDetails(localEventId: Long, localPersons: List<Person>?, currencies: List<Currency>): EventDetails {
        val primaryCurrency = currencies.first { it.id == currencyId }
        return EventDetails(
            event = Event(id = localEventId, serverId = id, name = name, pinCode = pinCode, primaryCurrencyId = primaryCurrency.id),
            currencies = currencies,
            persons = users.mapIndexed { i, dto ->
                dto.toPerson(localPersonId = localPersons?.getOrNull(i)?.takeIf { it.name == dto.name }?.id)
            },
            primaryCurrency = primaryCurrency,
        )
    }

    private fun Person.toCreateUserDto(): CreateUserDto {
        return CreateUserDto(name = name)
    }

    private fun UserDto.toPerson(localPersonId: Long?): Person {
        return Person(id = localPersonId ?: 0L, serverId = id, name = name)
    }

}
