package com.inwords.expenses.feature.expenses.ui.list

import com.inwords.expenses.feature.events.domain.EventsSyncStateHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalAtomicApi::class)
internal class PullToRefreshStateManager(
    private val eventsSyncStateHolder: EventsSyncStateHolder
) {

    private val userTriggeredRefreshEventId = MutableStateFlow<Long?>(null)

    private val job = AtomicReference<Job?>(null)

    fun isEventRefreshing(eventId: Long): Flow<Boolean> {
        return userTriggeredRefreshEventId
            .map { it == eventId }
            .distinctUntilChanged()
    }

    @OptIn(FlowPreview::class)
    fun onUserTriggeredRefresh(scope: CoroutineScope, eventId: Long) {
        scope.launch(start = CoroutineStart.UNDISPATCHED) {
            userTriggeredRefreshEventId.value = eventId
            job.exchange(coroutineContext[Job])?.cancelAndJoin()
            userTriggeredRefreshEventId.value = eventId // in case it was reset while waiting for cancellation

            val startedAt = Clock.System.now()

            val syncState = eventsSyncStateHolder.getStateFor(eventId)
                .debounce { isSyncing -> if (isSyncing) Duration.ZERO else 500.milliseconds }

            val started = withTimeoutOrNull(MIN_REFRESH_DISPLAY_TIME) {
                syncState.firstOrNull { it }
            } != null

            if (started) {
                withTimeoutOrNull(10.seconds) {
                    syncState.first { !it }
                }
            }

            delay(MIN_REFRESH_DISPLAY_TIME - (Clock.System.now() - startedAt))

            userTriggeredRefreshEventId.value = null
        }
    }

    private companion object {

        private val MIN_REFRESH_DISPLAY_TIME = 1500.milliseconds
    }
}
