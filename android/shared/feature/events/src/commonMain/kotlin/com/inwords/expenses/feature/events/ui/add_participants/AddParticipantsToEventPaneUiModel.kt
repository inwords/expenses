package com.inwords.expenses.feature.events.ui.add_participants

internal data class AddParticipantsToEventPaneUiModel(
    val participants: List<String>,
) {

    val isConfirmEnabled: Boolean by lazy(mode = LazyThreadSafetyMode.PUBLICATION) {
        participants.any { it.trim().isNotBlank() }
    }
}
