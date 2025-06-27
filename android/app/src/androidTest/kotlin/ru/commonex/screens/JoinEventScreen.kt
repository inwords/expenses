package ru.commonex.screens

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import de.mannodermaus.junit5.compose.ComposeContext

internal class JoinEventScreen : BaseScreen() {

    context(extension: ComposeContext)
    fun enterEventId(eventId: String): JoinEventScreen {
        extension.onNodeWithText("ID события").performTextInput(eventId)
        return this
    }

    context(extension: ComposeContext)
    fun enterAccessCode(accessCode: String): JoinEventScreen {
        extension.onNodeWithText("Код доступа").performTextInput(accessCode)
        return this
    }

    context(extension: ComposeContext)
    fun clickConfirmButton(): ChoosePersonScreen {
        extension.onNodeWithText("Участники").performClick()
        return ChoosePersonScreen()
    }

    context(extension: ComposeContext)
    fun joinEvent(eventId: String, accessCode: String): ChoosePersonScreen {
        enterEventId(eventId)
        enterAccessCode(accessCode)
        return clickConfirmButton()
    }
}
