package com.inwords.expenses.feature.events.ui.local

import com.inwords.expenses.feature.events.domain.EventsInteractor
import kotlinx.collections.immutable.ImmutableList

data class LocalEventsUiModel(
    val events: ImmutableList<LocalEventUiModel>,
    val recentlyRemovedEventName: String?,
) {

    data class LocalEventUiModel(
        val eventId: Long,
        val eventName: String,
        val deletionState: EventsInteractor.EventDeletionState,
    )
}
