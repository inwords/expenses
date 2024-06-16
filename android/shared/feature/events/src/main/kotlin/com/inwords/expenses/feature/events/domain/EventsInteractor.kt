package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.flatMapLatestNoBuffer
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.settings.api.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class EventsInteractor internal constructor(
    private val eventsRepository: EventsRepository,
    private val settingsRepository: SettingsRepository,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + IO)
) {

    internal sealed interface JoinEventResult {
        data class NewCurrentEvent(val event: Event) : JoinEventResult
        data object InvalidAccessCode : JoinEventResult
        data object EventNotFound : JoinEventResult
    }

    val currentEvent: StateFlow<Event?> = settingsRepository.getCurrentEventId()
        .flatMapLatestNoBuffer { currentEventId ->
            if (currentEventId == null) {
                flowOf(null)
            } else {
                eventsRepository.getEvent(currentEventId)
            }
        }
        .stateIn(scope, started = SharingStarted.WhileSubscribed(), initialValue = null)

    fun getEventDetails(event: Event): Flow<EventDetails> {
        return eventsRepository.getEventWithDetails(event.id)
    }

    internal suspend fun joinEvent(eventId: Long, accessCode: String): JoinEventResult {
        return if (eventId != 1L) {
            JoinEventResult.EventNotFound
        } else if (accessCode != "1234") {
            JoinEventResult.InvalidAccessCode
        } else {
            val newCurrentEvent = Event(1L, "Fruska")
            settingsRepository.setCurrentEventId(newCurrentEvent.id)
            settingsRepository.setCurrentPersonId(1L) // TODO
            JoinEventResult.NewCurrentEvent(newCurrentEvent)
        }
    }

    private var draftEventName = ""
    private var draftOwner = ""
    private var draftOtherPersons = emptyList<String>()

    internal suspend fun draftEventName(eventName: String) {
        draftEventName = eventName.trim() // FIXME save to storage
    }

    internal suspend fun draftOwner(owner: String) {
        draftOwner = owner.trim()
    }

    internal suspend fun draftOtherPersons(persons: List<String>) {
        draftOtherPersons = persons.map { it.trim() }.filter { it.isNotEmpty() }
    }

    internal suspend fun createEvent(): EventDetails {
        val personsToInsert = (listOf(draftOwner) + draftOtherPersons).map { personName ->
            Person(0L, personName)
        }
        // FIXME stub
        val currenciesToInsert = listOf(
            Currency(1L, "USD", "US Dollar"),
            Currency(2L, "EUR", "Euro"),
            Currency(3L, "RUB", "Russian Ruble"),
        )
        val eventToInsert = Event(0L, draftEventName)

        val eventDetails = eventsRepository.deepInsert(
            eventToInsert = eventToInsert,
            currenciesToInsert = currenciesToInsert,
            personsToInsert = personsToInsert,
            primaryCurrencyIndex = 1,
        )

        settingsRepository.setCurrentEventId(eventDetails.event.id)
        settingsRepository.setCurrentPersonId(eventDetails.persons.first().id)

        return eventDetails
    }

}