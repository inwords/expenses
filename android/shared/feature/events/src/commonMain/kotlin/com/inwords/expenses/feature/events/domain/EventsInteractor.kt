package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.core.locator.ComponentsMap
import com.inwords.expenses.core.locator.getComponent
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.flatMapLatestNoBuffer
import com.inwords.expenses.feature.events.api.EventsComponent
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import com.inwords.expenses.feature.settings.api.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlin.random.Random

class EventsInteractor internal constructor(
    private val eventsLocalStore: EventsLocalStore,
    private val settingsRepository: SettingsRepository,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + IO)
) {

    internal sealed interface JoinEventResult {
        data class NewCurrentEvent(val event: Event) : JoinEventResult
        data object InvalidAccessCode : JoinEventResult
        data object EventNotFound : JoinEventResult
    }

    val currentEvent: StateFlow<EventDetails?> = settingsRepository.getCurrentEventId()
        .flatMapLatestNoBuffer { currentEventId ->
            if (currentEventId == null) {
                flowOf(null)
            } else {
                eventsLocalStore.getEventWithDetails(currentEventId)
            }
        }
        .stateIn(scope, started = SharingStarted.WhileSubscribed(), initialValue = null)

    fun getEventDetails(event: Event): Flow<EventDetails> {
        return eventsLocalStore.getEventWithDetails(event.id)
    }

    internal suspend fun joinEvent(eventId: Long, accessCode: String): JoinEventResult {
        return if (eventId != 1L) {
            JoinEventResult.EventNotFound
        } else if (accessCode != "1234") {
            JoinEventResult.InvalidAccessCode
        } else {
            val newCurrentEvent = Event(1L, 11L, "Fruska", "1234")
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
            Person(0L, 0L, personName)
        }

        val eventToInsert = Event(
            id = 0L,
            serverId = 0L,
            name = draftEventName,
            // FIXME secure
            pinCode = Random.Default.nextLong(1000, 9999).toString()
        )

        val eventDetails = eventsLocalStore.deepInsert(
            eventToInsert = eventToInsert,
            personsToInsert = personsToInsert,
            primaryCurrencyIndex = 0,
        )

        // FIXME not here
        ComponentsMap.getComponent<EventsComponent>().eventSyncTask.syncEvent(eventDetails.event.id)

        settingsRepository.setCurrentEventId(eventDetails.event.id)
        settingsRepository.setCurrentPersonId(eventDetails.persons.first().id)

        return eventDetails
    }

}