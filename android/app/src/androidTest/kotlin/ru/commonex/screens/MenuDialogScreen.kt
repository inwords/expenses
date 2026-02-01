package ru.commonex.screens

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isEnabled
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import expenses.shared.feature.menu.generated.resources.Res
import expenses.shared.feature.menu.generated.resources.menu_add_participants_action
import expenses.shared.feature.menu.generated.resources.menu_choose_person_action
import expenses.shared.feature.menu.generated.resources.menu_copy_action
import expenses.shared.feature.menu.generated.resources.menu_open_events_list
import org.jetbrains.compose.resources.getString

@OptIn(ExperimentalTestApi::class)
internal class MenuDialogScreen : BaseScreen() {

    context(rule: ComposeTestRule)
    suspend fun openEventsList(): LocalEventsScreen {
        val openEventsLabel = getString(Res.string.menu_open_events_list)
        rule.onNodeWithText(openEventsLabel).performClick()
        return LocalEventsScreen().waitUntilLoaded()
    }

    context(rule: ComposeTestRule)
    suspend fun chooseParticipant(): ChoosePersonScreen {
        val choosePersonLabel = getString(Res.string.menu_choose_person_action)
        rule.onNodeWithText(choosePersonLabel).performClick()
        return ChoosePersonScreen()
    }

    context(rule: ComposeTestRule)
    suspend fun addParticipant(): AddParticipantsToEventScreen {
        val addParticipantsLabel = getString(Res.string.menu_add_participants_action)
        rule.onNodeWithText(addParticipantsLabel).performClick()
        return AddParticipantsToEventScreen()
    }

    context(rule: ComposeTestRule)
    suspend fun waitUntilCopyEnabled(timeout: Long = 10000): MenuDialogScreen {
        val copyLabel = getString(Res.string.menu_copy_action)
        rule.waitUntilAtLeastOneExists(hasText(copyLabel) and isEnabled(), timeout)
        return this
    }

    context(rule: ComposeTestRule)
    suspend fun clickCopyShareLink(): MenuDialogScreen {
        val copyLabel = getString(Res.string.menu_copy_action)
        rule.onNodeWithText(copyLabel).performClick()
        return this
    }
}
