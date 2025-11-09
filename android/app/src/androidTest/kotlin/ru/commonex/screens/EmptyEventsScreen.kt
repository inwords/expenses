package ru.commonex.screens

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import de.mannodermaus.junit5.compose.ComposeContext

internal class EmptyEventsScreen : BaseScreen() {
    context(extension: ComposeContext)
    fun clickCreateEvent(): CreateEventScreen {
        extension.onNodeWithText("Создать").performClick()
        return CreateEventScreen()
    }

    context(extension: ComposeContext)
    fun clickJoinEvent(): JoinEventScreen {
        extension.onNodeWithText("Присоединиться").performClick()
        return JoinEventScreen()
    }

}
