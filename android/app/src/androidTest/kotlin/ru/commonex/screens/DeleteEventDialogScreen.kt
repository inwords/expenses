package ru.commonex.screens

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import de.mannodermaus.junit5.compose.ComposeContext

internal class DeleteEventDialogScreen : BaseScreen() {

    context(extension: ComposeContext)
    suspend fun confirmDeletion(): LocalEventsScreen {
        extension
            .onNodeWithTag("delete_event_dialog_delete_button")
            .performClick()
        return LocalEventsScreen().waitUntilLoaded()
    }

    context(extension: ComposeContext)
    suspend fun keepEvent(): LocalEventsScreen {
        extension
            .onNodeWithTag("delete_event_dialog_keep_button")
            .performClick()
        return LocalEventsScreen().waitUntilLoaded()
    }
}
