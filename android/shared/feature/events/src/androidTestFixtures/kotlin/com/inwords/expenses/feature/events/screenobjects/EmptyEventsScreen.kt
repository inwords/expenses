package com.inwords.expenses.feature.events.screenobjects

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.inwords.expenses.feature.expenses.screenobjects.BaseScreen
import de.mannodermaus.junit5.compose.ComposeContext

class EmptyEventsScreen : BaseScreen() {

    private companion object {
        const val CREATE_BUTTON_LABEL = "Create"
        const val JOIN_BUTTON_LABEL = "Join"
    }

    context(extension: ComposeContext)
    suspend fun clickCreateEvent(): CreateEventScreen {
        extension.onNodeWithText(CREATE_BUTTON_LABEL).performClick()
        return CreateEventScreen()
    }

    context(extension: ComposeContext)
    suspend fun clickJoinEvent(): JoinEventScreen {
        extension.onNodeWithText(JOIN_BUTTON_LABEL).performClick()
        return JoinEventScreen()
    }
}
