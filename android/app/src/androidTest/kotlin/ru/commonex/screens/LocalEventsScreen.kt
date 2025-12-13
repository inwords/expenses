package ru.commonex.screens

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import de.mannodermaus.junit5.compose.ComposeContext
import expenses.shared.feature.events.generated.resources.Res
import expenses.shared.feature.events.generated.resources.events_create
import expenses.shared.feature.events.generated.resources.events_join
import org.jetbrains.compose.resources.getString

internal class LocalEventsScreen : BaseScreen() {

    context(extension: ComposeContext)
    suspend fun waitUntilLoaded(): LocalEventsScreen {
        val createLabel = getString(Res.string.events_create)
        waitForElementWithText(createLabel)
        return this
    }

    context(extension: ComposeContext)
    suspend fun clickCreateEvent(): CreateEventScreen {
        val createLabel = getString(Res.string.events_create)
        extension.onNodeWithText(createLabel).performClick()
        return CreateEventScreen()
    }

    context(extension: ComposeContext)
    suspend fun clickJoinEvent(): JoinEventScreen {
        val joinLabel = getString(Res.string.events_join)
        extension.onNodeWithText(joinLabel).performClick()
        return JoinEventScreen()
    }

    context(extension: ComposeContext)
    fun swipeToDelete(eventName: String): DeleteEventDialogScreen {
        waitForElementWithText(eventName)
        extension.onNodeWithText(eventName).performTouchInput { swipeLeft() }
        return DeleteEventDialogScreen()
    }

    context(extension: ComposeContext)
    fun assertEventExists(eventName: String): LocalEventsScreen {
        assertElementWithTextExists(eventName)
        return this
    }

    context(extension: ComposeContext)
    fun assertEventNotExists(eventName: String): LocalEventsScreen {
        waitForElementWithTextDoesNotExist(eventName)
        return this
    }

}
