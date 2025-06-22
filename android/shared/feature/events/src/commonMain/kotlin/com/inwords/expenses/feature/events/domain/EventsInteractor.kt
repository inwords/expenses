package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.flatMapLatestNoBuffer
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.events.domain.store.local.CurrenciesLocalStore
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
    eventsLocalStoreLazy: Lazy<EventsLocalStore>,
    currenciesLocalStoreLazy: Lazy<CurrenciesLocalStore>,
    settingsRepositoryLazy: Lazy<SettingsRepository>,
    joinRemoteEventUseCaseLazy: Lazy<JoinRemoteEventUseCase>,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + IO)
) {

    private val eventsLocalStore by eventsLocalStoreLazy
    private val currenciesLocalStore by currenciesLocalStoreLazy
    private val settingsRepository by settingsRepositoryLazy
    private val joinRemoteEventUseCase by joinRemoteEventUseCaseLazy

    internal sealed interface JoinEventResult {
        data class NewCurrentEvent(val event: EventDetails) : JoinEventResult
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
                eventsLocalStore.getEventWithDetailsFlow(currentEventId)
            }
        }
        .stateIn(scope, started = SharingStarted.WhileSubscribed(), initialValue = null)

    fun getCurrencies(): Flow<List<Currency>> {
        return currenciesLocalStore.getCurrencies()
    }

    suspend fun leaveEvent() {
        settingsRepository.clearCurrentEventId()
        settingsRepository.clearCurrentPersonId()
    }

    internal suspend fun joinEvent(eventServerId: String, accessCode: String): JoinEventResult {
        val localEvent = eventsLocalStore.getEventWithDetailsByServerId(eventServerId)
        if (localEvent != null) {
            settingsRepository.setCurrentEventId(localEvent.event.id)
            return JoinEventResult.NewCurrentEvent(localEvent)
        }

        val joinEventResult = joinRemoteEventUseCase.joinRemoteEvent(
            event = Event(0L, eventServerId, "", accessCode, 0L),
            localCurrencies = null,
            localPersons = null,
        )

        when (joinEventResult) {
            is JoinEventResult.NewCurrentEvent -> {
                settingsRepository.setCurrentEventId(joinEventResult.event.event.id)
            }

            JoinEventResult.EventNotFound,
            JoinEventResult.InvalidAccessCode,
            JoinEventResult.OtherError -> Unit
        }

        return joinEventResult
    }

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
            pinCode = Random.Default.nextLong(1000, 9999).toString(),
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