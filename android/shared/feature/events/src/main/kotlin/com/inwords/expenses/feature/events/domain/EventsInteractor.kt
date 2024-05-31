package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.events.domain.model.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EventsInteractor internal constructor(
    private val eventsRepository: EventsRepository,
    private val personsRepository: PersonsRepository,
    private val currenciesRepository: CurrenciesRepository,
) {

    internal sealed interface JoinEventResult {
        data class NewCurrentEvent(val event: Event) : JoinEventResult
        data object InvalidAccessCode : JoinEventResult
        data object EventNotFound : JoinEventResult
    }

    private val _currentEvent = MutableStateFlow<Event?>(null)
    val currentEvent: StateFlow<Event?> = _currentEvent

    internal suspend fun joinEvent(eventId: Long, accessCode: String): JoinEventResult {
        return if (eventId != 1L) {
            JoinEventResult.EventNotFound
        } else if (accessCode != "1234") {
            JoinEventResult.InvalidAccessCode
        } else {
            val newCurrentEvent = Event(1L, "Fruska")
            _currentEvent.value = newCurrentEvent
            JoinEventResult.NewCurrentEvent(newCurrentEvent)
        }
    }

    private var draftEventName = ""
    private var draftOwner = ""
    private var draftPersons = emptyList<String>()

    internal suspend fun draftEventName(eventName: String) {
        draftEventName = eventName
    }

    internal suspend fun draftOwner(owner: String) {
        draftOwner = owner
    }

    internal suspend fun draftPersons(persons: List<String>) {
        draftPersons = persons
    }

    internal suspend fun createEvent(): EventDetails {
        // FIXME transaction
        val primaryPerson = personsRepository.insert(Person(0L, draftOwner))
        val persons = draftPersons.map { personName ->
            personsRepository.insert(Person(0L, personName))
        }

        // FIXME stub
        val currencies = listOf(
            Currency(1L, "USD", "US Dollar"),
            Currency(2L, "EUR", "Euro"),
            Currency(3L, "RUB", "Russian Ruble"),
        ).map { currency ->
            currenciesRepository.insert(currency)
        }

        val newEvent = Event(0L, draftEventName)

        val eventDetails = eventsRepository.insert(
            EventDetails(
                event = newEvent,
                currencies = currencies,
                persons = persons,
                primaryCurrency = currencies[0],
                primaryPerson = primaryPerson,
            )
        )

        _currentEvent.value = eventDetails.event

        return eventDetails
    }


    fun getEventDetails(event: Event): Flow<EventDetails> {
        return eventsRepository.getEventWithDetails(event.id)
    }

}