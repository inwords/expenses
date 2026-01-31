package com.inwords.expenses.feature.menu.ui

internal data class MenuDialogUiModel(
    val eventName: String,
    val shareState: ShareState,
) {

    sealed interface ShareState {
        data class Idle(val serverId: String?, val pinCode: String) : ShareState
        data object Loading : ShareState
        data class Ready(val shareText: ShareText) : ShareState
        data class PendingClipboardCopy(val shareText: ShareText) : ShareState

        companion object {

            val ShareState.canShare
                get() = when (this) {
                    is Idle -> serverId != null
                    Loading -> false
                    is PendingClipboardCopy,
                    is Ready -> true
                }
        }
    }

    /**
     * [eventName] - title of the share content
     * [fullText] - full text to share (with title)
     */
    class ShareText(
        val eventName: String,
        val fullText: String,
    )
}
