package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.flatMapLatestNoBuffer
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import com.inwords.expenses.feature.events.domain.store.remote.EventsRemoteStore
import com.inwords.expenses.feature.settings.api.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlin.random.Random

class EventsInteractor internal constructor(
    private val eventsLocalStore: EventsLocalStore,
    private val eventsRemoteStore: EventsRemoteStore,
    private val currenciesPullTask: CurrenciesPullTask,
    private val settingsRepository: SettingsRepository,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + IO)
) {

    internal sealed interface JoinEventResult {
        data class NewCurrentEvent(val event: Event) : JoinEventResult
        data object InvalidAccessCode : JoinEventResult
        data object EventNotFound : JoinEventResult
        data object OtherError : JoinEventResult
    }

    private val draft = Draft()

    val currentEvent: StateFlow<EventDetails?> = settingsRepository.getCurrentEventId()
        .flatMapLatestNoBuffer { currentEventId ->
            if (currentEventId == null) {
                flowOf(null)
            } else {
                eventsLocalStore.getEventWithDetails(currentEventId)
            }
        }
        .stateIn(scope, started = SharingStarted.WhileSubscribed(), initialValue = null)

    internal suspend fun joinEvent(eventServerId: Long, accessCode: String): JoinEventResult {
        val localEvent = eventsLocalStore.getEventWithDetailsByServerId(eventServerId)
        if (localEvent != null) {
            settingsRepository.setCurrentEventId(localEvent.event.id)
            settingsRepository.setCurrentPersonId(localEvent.persons.first().id) // FIXME select person on UI
            return JoinEventResult.NewCurrentEvent(localEvent.event)
        }

        return when (val eventResult = eventsRemoteStore.getEvent(eventServerId, accessCode)) {
            is EventsRemoteStore.GetEventResult.Event -> {
                val remoteEvent = eventResult.event

                val updatedCurrencies = currenciesPullTask.updateLocalCurrencies(remoteEvent.currencies)

                val eventDetails = eventsLocalStore.deepInsert(
                    eventToInsert = remoteEvent.event,
                    personsToInsert = remoteEvent.persons,
                    primaryCurrencyId = updatedCurrencies.first { it.serverId == remoteEvent.primaryCurrency.serverId }.id,
                    prefetchedLocalCurrencies = updatedCurrencies,
                )

                settingsRepository.setCurrentEventId(eventDetails.event.id)
                settingsRepository.setCurrentPersonId(eventDetails.persons.first().id) // FIXME select person on UI

                JoinEventResult.NewCurrentEvent(eventDetails.event)
            }

            EventsRemoteStore.GetEventResult.EventNotFound -> JoinEventResult.EventNotFound
            EventsRemoteStore.GetEventResult.InvalidAccessCode -> JoinEventResult.InvalidAccessCode
            EventsRemoteStore.GetEventResult.OtherError -> JoinEventResult.OtherError
        }
    }

    internal fun draftEventName(eventName: String) {
        draft.draftEventName = eventName.trim()
    }

    internal fun draftOwner(owner: String) {
        draft.draftOwner = owner.trim()
    }

    internal fun draftOtherPersons(persons: List<String>) {
        draft.draftOtherPersons = persons.map { it.trim() }.filter { it.isNotEmpty() }
    }

    internal suspend fun createEvent(): EventDetails {
        val personsToInsert = (listOf(draft.draftOwner) + draft.draftOtherPersons).map { personName ->
            Person(0L, 0L, personName)
        }

        val eventToInsert = Event(
            id = 0L,
            serverId = 0L,
            name = draft.draftEventName,
            // FIXME secure
            pinCode = Random.Default.nextLong(1000, 9999).toString()
        )

        val eventDetails = eventsLocalStore.deepInsert(
            eventToInsert = eventToInsert,
            personsToInsert = personsToInsert,
            primaryCurrencyId = 1,
        )

        settingsRepository.setCurrentEventId(eventDetails.event.id)
        settingsRepository.setCurrentPersonId(eventDetails.persons.first().id)

        draft.clear()

        return eventDetails
    }

    private class Draft(
        var draftEventName: String = "",
        var draftOwner: String = "",
        var draftOtherPersons: List<String> = emptyList()
    ) {

        fun clear() {
            draftEventName = ""
            draftOwner = ""
            draftOtherPersons = emptyList()
        }
    }

}