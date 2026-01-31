package ru.commonex.screens

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import expenses.shared.feature.events.generated.resources.Res
import expenses.shared.feature.events.generated.resources.events_access_code_placeholder
import expenses.shared.feature.events.generated.resources.events_id_placeholder
import expenses.shared.feature.events.generated.resources.events_participants_title
import org.jetbrains.compose.resources.getString

internal class JoinEventScreen : BaseScreen() {

    context(rule: ComposeTestRule)
    suspend fun enterEventId(eventId: String): JoinEventScreen {
        val idLabel = getString(Res.string.events_id_placeholder)
        rule.onNodeWithText(idLabel).performTextInput(eventId)
        return this
    }

    context(rule: ComposeTestRule)
    suspend fun enterAccessCode(accessCode: String): JoinEventScreen {
        val accessCodeLabel = getString(Res.string.events_access_code_placeholder)
        rule.onNodeWithText(accessCodeLabel).performTextInput(accessCode)
        return this
    }

    context(rule: ComposeTestRule)
    suspend fun clickConfirmButton(): ChoosePersonScreen {
        val participantsLabel = getString(Res.string.events_participants_title)
        rule.onNodeWithText(participantsLabel).performClick()
        return ChoosePersonScreen()
    }

    context(rule: ComposeTestRule)
    suspend fun joinEvent(eventId: String, accessCode: String): ChoosePersonScreen {
        enterEventId(eventId)
        enterAccessCode(accessCode)
        return clickConfirmButton()
    }
}
