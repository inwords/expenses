package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.flatMapLatestNoBuffer
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import com.inwords.expenses.feature.settings.api.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class EventsInteractor internal constructor(
    eventsLocalStoreLazy: Lazy<EventsLocalStore>,
    settingsRepositoryLazy: Lazy<SettingsRepository>,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + IO)
) {

    private val eventsLocalStore by eventsLocalStoreLazy
    private val settingsRepository by settingsRepositoryLazy

    sealed interface EventDeletionState {
        data object None : EventDeletionState
        data object Loading : EventDeletionState
        data object RemoteDeletionFailed : EventDeletionState
    }

    private val draft = Draft()

    private val _eventsFailedToDeleteRemotely = MutableStateFlow<Map<Long, EventDeletionState>>(emptyMap())
    val eventsDeletionState: StateFlow<Map<Long, EventDeletionState>> = _eventsFailedToDeleteRemotely

    val currentEvent: StateFlow<EventDetails?> = settingsRepository.getCurrentEventId()
        .flatMapLatestNoBuffer { currentEventId ->
            if (currentEventId == null) {
                flowOf(null)
            } else {
                eventsLocalStore.getEventWithDetailsFlow(currentEventId)
            }
        }
        .stateIn(scope, started = SharingStarted.WhileSubscribed(), initialValue = null)

    internal fun draftEventName(eventName: String) {
        draft.draftEventName = eventName.trim()
    }

    internal fun draftEventPrimaryCurrency(currency: Currency) {
        draft.draftPrimaryCurrencyId = currency.id
    }

    internal fun draftOwner(owner: String) {
        draft.draftOwner = owner.trim()
    }

    internal fun draftOtherPersons(persons: List<String>) {
        draft.draftOtherPersons = persons.map { it.trim() }.filter { it.isNotEmpty() }
    }

    internal suspend fun createEvent(): EventDetails {
        val personsToInsert = (listOf(draft.draftOwner) + draft.draftOtherPersons).map { personName ->
            Person(0L, null, personName)
        }

        val eventToInsert = Event(
            id = 0L,
            serverId = null,
            name = draft.draftEventName,
            // FIXME secure
            pinCode = Random.nextLong(1000, 9999).toString(),
            primaryCurrencyId = draft.draftPrimaryCurrencyId
        )

        val eventDetails = eventsLocalStore.deepInsert(
            eventToInsert = eventToInsert,
            personsToInsert = personsToInsert,
            inTransaction = true
        )

        settingsRepository.setCurrentEventId(eventDetails.event.id)
        settingsRepository.setCurrentPersonId(eventDetails.persons.first().id)

        draft.clear()

        return eventDetails
    }

    internal fun setEventDeletionState(eventId: Long, state: EventDeletionState) {
        _eventsFailedToDeleteRemotely.update { it + (eventId to state) }
    }

    fun clearEventDeletionState(eventId: Long) {
        _eventsFailedToDeleteRemotely.update { it - eventId }
    }

    private class Draft(
        var draftEventName: String = "",
        var draftPrimaryCurrencyId: Long = 0,
        var draftOwner: String = "",
        var draftOtherPersons: List<String> = emptyList()
    ) {

        fun clear() {
            draftEventName = ""
            draftPrimaryCurrencyId = 0
            draftOwner = ""
            draftOtherPersons = emptyList()
        }
    }

}