package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.flatMapLatestNoBuffer
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import com.inwords.expenses.feature.settings.api.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class GetCurrentEventStateUseCase internal constructor(
    eventsLocalStoreLazy: Lazy<EventsLocalStore>,
    settingsRepositoryLazy: Lazy<SettingsRepository>,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + IO)
) {

    private val eventsLocalStore by eventsLocalStoreLazy
    private val settingsRepository by settingsRepositoryLazy

    val currentEvent: StateFlow<EventDetails?> = settingsRepository.getCurrentEventId()
        .flatMapLatestNoBuffer { currentEventId ->
            if (currentEventId == null) {
                flowOf(null)
            } else {
                eventsLocalStore.getEventWithDetailsFlow(currentEventId)
            }
        }
        .stateIn(scope, started = SharingStarted.WhileSubscribed(), initialValue = null)
}
