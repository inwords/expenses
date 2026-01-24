package ru.commonex.screens

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import de.mannodermaus.junit5.compose.ComposeContext
import expenses.shared.feature.menu.generated.resources.Res
import expenses.shared.feature.menu.generated.resources.menu_add_participants_action
import expenses.shared.feature.menu.generated.resources.menu_choose_person_action
import expenses.shared.feature.menu.generated.resources.menu_open_events_list
import org.jetbrains.compose.resources.getString

internal class MenuDialogScreen : BaseScreen() {

    context(extension: ComposeContext)
    suspend fun openEventsList(): LocalEventsScreen {
        val openEventsLabel = getString(Res.string.menu_open_events_list)
        extension.onNodeWithText(openEventsLabel).performClick()
        return LocalEventsScreen().waitUntilLoaded()
    }

    context(extension: ComposeContext)
    suspend fun chooseParticipant(): ChoosePersonScreen {
        val choosePersonLabel = getString(Res.string.menu_choose_person_action)
        extension.onNodeWithText(choosePersonLabel).performClick()
        return ChoosePersonScreen()
    }

    context(extension: ComposeContext)
    suspend fun addParticipant(): AddParticipantsToEventScreen {
        val addParticipantsLabel = getString(Res.string.menu_add_participants_action)
        extension.onNodeWithText(addParticipantsLabel).performClick()
        return AddParticipantsToEventScreen()
    }
}
