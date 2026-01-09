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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalAtomicApi::class)
internal class PullToRefreshStateManager(
    private val eventsSyncStateHolder: EventsSyncStateHolder
) {

    private val userTriggeredRefresh = MutableStateFlow(false)

    private val job = AtomicReference<Job?>(null)

    @OptIn(FlowPreview::class)
    fun isEventRefreshing(eventId: Long): Flow<Boolean> {
        val syncState = eventsSyncStateHolder.getStateFor(eventId)
            .debounce { isSyncing -> if (isSyncing) Duration.ZERO else 500.milliseconds }

        return combine(
            syncState,
            userTriggeredRefresh
        ) { isSyncing, isUserTriggered ->
            isSyncing || isUserTriggered
        }
    }

    fun onUserTriggeredRefresh(scope: CoroutineScope) {
        scope.launch(start = CoroutineStart.UNDISPATCHED) {
            userTriggeredRefresh.value = true
            job.exchange(coroutineContext[Job])?.cancelAndJoin()
            userTriggeredRefresh.value = true // in case it was reset while waiting for cancellation
            delay(1.5.seconds)
            userTriggeredRefresh.value = false
        }
    }
}