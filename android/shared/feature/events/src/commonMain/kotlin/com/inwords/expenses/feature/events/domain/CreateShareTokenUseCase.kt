package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.feature.events.domain.model.EventShareToken
import com.inwords.expenses.feature.events.domain.store.remote.EventsRemoteStore

class CreateShareTokenUseCase internal constructor(
    eventsRemoteStoreLazy: Lazy<EventsRemoteStore>,
) {

    sealed interface CreateShareTokenResult {
        data class Created(val token: EventShareToken) : CreateShareTokenResult
        data object RemoteFailed : CreateShareTokenResult
    }

    private val eventsRemoteStore by eventsRemoteStoreLazy

    suspend fun createShareToken(eventServerId: String, pinCode: String): CreateShareTokenResult {
        // TODO: Add analytics when fallback (PIN-based) link is used
        return eventsRemoteStore.createEventShareToken(eventServerId, pinCode)
    }
}
