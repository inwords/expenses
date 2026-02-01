package ru.commonex.screens

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick

internal class DeleteEventDialogScreen : BaseScreen() {

    context(rule: ComposeTestRule)
    suspend fun confirmDeletion(): LocalEventsScreen {
        rule
            .onNodeWithTag("delete_event_dialog_delete_button")
            .performClick()
        return LocalEventsScreen().waitUntilLoaded()
    }

    context(rule: ComposeTestRule)
    suspend fun keepEvent(): LocalEventsScreen {
        rule
            .onNodeWithTag("delete_event_dialog_keep_button")
            .performClick()
        return LocalEventsScreen().waitUntilLoaded()
    }
}
