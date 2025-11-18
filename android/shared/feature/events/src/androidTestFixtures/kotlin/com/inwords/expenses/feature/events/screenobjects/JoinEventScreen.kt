package com.inwords.expenses.feature.events.screenobjects

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.inwords.expenses.feature.expenses.screenobjects.BaseScreen
import de.mannodermaus.junit5.compose.ComposeContext
import expenses.shared.feature.events.generated.resources.Res
import expenses.shared.feature.events.generated.resources.events_access_code_placeholder
import expenses.shared.feature.events.generated.resources.events_id_placeholder
import expenses.shared.feature.events.generated.resources.events_participants_title
import org.jetbrains.compose.resources.getString

class JoinEventScreen : BaseScreen() {

    context(extension: ComposeContext)
    suspend fun enterEventId(eventId: String): JoinEventScreen {
        val idLabel = getString(Res.string.events_id_placeholder)
        extension.onNodeWithText(idLabel).performTextInput(eventId)
        return this
    }

    context(extension: ComposeContext)
    suspend fun enterAccessCode(accessCode: String): JoinEventScreen {
        val accessCodeLabel = getString(Res.string.events_access_code_placeholder)
        extension.onNodeWithText(accessCodeLabel).performTextInput(accessCode)
        return this
    }

    context(extension: ComposeContext)
    suspend fun clickConfirmButton(): ChoosePersonScreen {
        val participantsLabel = getString(Res.string.events_participants_title)
        extension.onNodeWithText(participantsLabel).performClick()
        return ChoosePersonScreen()
    }

    context(extension: ComposeContext)
    suspend fun joinEvent(eventId: String, accessCode: String): ChoosePersonScreen {
        enterEventId(eventId)
        enterAccessCode(accessCode)
        return clickConfirmButton()
    }
}
