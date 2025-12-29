package ru.commonex.screens

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import de.mannodermaus.junit5.compose.ComposeContext
import expenses.shared.feature.events.generated.resources.Res
import expenses.shared.feature.events.generated.resources.events_create
import expenses.shared.feature.events.generated.resources.events_create_join_description
import expenses.shared.feature.events.generated.resources.events_delete_everywhere
import expenses.shared.feature.events.generated.resources.events_delete_local_only
import expenses.shared.feature.events.generated.resources.events_event_deleted
import expenses.shared.feature.events.generated.resources.events_join
import expenses.shared.feature.events.generated.resources.events_keep_event
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
    fun clickEvent(eventName: String): ChoosePersonScreen {
        waitForElementWithText(eventName)
        extension.onNodeWithText(eventName).performClick()
        return ChoosePersonScreen()
    }

    context(extension: ComposeContext)
    fun swipeToRevealActions(eventName: String): LocalEventsScreen {
        waitForElementWithText(eventName)
        extension.onNodeWithText(eventName).performTouchInput { swipeLeft() }
        return this
    }

    context(extension: ComposeContext)
    suspend fun clickDeleteEverywhere(): DeleteEventDialogScreen {
        val deleteEverywhereLabel = getString(Res.string.events_delete_everywhere)
        waitForElementWithText(deleteEverywhereLabel)
        extension.onNodeWithText(deleteEverywhereLabel).performClick()
        return DeleteEventDialogScreen()
    }

    context(extension: ComposeContext)
    suspend fun clickDeleteLocalOnly(): LocalEventsScreen {
        val deleteLocalOnlyLabel = getString(Res.string.events_delete_local_only)
        waitForElementWithText(deleteLocalOnlyLabel)
        extension.onNodeWithText(deleteLocalOnlyLabel).performClick()
        return this
    }

    context(extension: ComposeContext)
    suspend fun clickKeepEvent(): LocalEventsScreen {
        val keepEventLabel = getString(Res.string.events_keep_event)
        waitForElementWithText(keepEventLabel)
        extension.onNodeWithText(keepEventLabel).performClick()
        return this
    }

    context(extension: ComposeContext)
    fun swipeBack(eventName: String): LocalEventsScreen {
        waitForElementWithText(eventName)
        extension.onNodeWithText(eventName).performTouchInput { swipeRight() }
        return this
    }

    context(extension: ComposeContext)
    fun assertEventExists(eventName: String): LocalEventsScreen {
        waitForElementWithText(eventName)
        assertElementWithTextExists(eventName)
        return this
    }

    context(extension: ComposeContext)
    fun assertEventNotExists(eventName: String): LocalEventsScreen {
        waitForElementWithTextDoesNotExist(eventName)
        return this
    }

    context(extension: ComposeContext)
    suspend fun assertCreateJoinDescriptionVisible(): LocalEventsScreen {
        val description = getString(Res.string.events_create_join_description)
        waitForElementWithText(description)
        assertElementWithTextExists(description)
        return this
    }

    context(extension: ComposeContext)
    suspend fun assertEventDeletedSnackbar(eventName: String): LocalEventsScreen {
        val message = getString(Res.string.events_event_deleted, eventName)
        waitForElementWithText(message)
        return this
    }

}
