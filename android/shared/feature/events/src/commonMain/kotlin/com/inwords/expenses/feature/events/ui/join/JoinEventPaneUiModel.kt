package com.inwords.expenses.feature.events.ui.join

internal data class JoinEventPaneUiModel(
    val eventId: String,
    val eventAccessCode: String,
    val joining: EventJoiningState,
) {

    sealed interface EventJoiningState {
        object None : EventJoiningState
        object Joining : EventJoiningState
        data class Error(val message: String) : EventJoiningState
    }
}
