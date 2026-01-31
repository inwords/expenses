package ru.commonex.screens

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import expenses.shared.feature.events.generated.resources.Res
import expenses.shared.feature.events.generated.resources.events_name_placeholder
import expenses.shared.feature.events.generated.resources.events_participants_title
import org.jetbrains.compose.resources.getString

internal class CreateEventScreen : BaseScreen() {
    context(rule: ComposeTestRule)
    suspend fun enterEventName(name: String): CreateEventScreen {
        val nameLabel = getString(Res.string.events_name_placeholder)
        rule.onNodeWithText(nameLabel).performTextInput(name)
        return this
    }

    context(rule: ComposeTestRule)
    fun selectCurrency(currency: String): CreateEventScreen {
        rule.onNodeWithText(currency).performClick()
        return this
    }

    context(rule: ComposeTestRule)
    suspend fun clickContinueButton(): AddPersonsScreen {
        val participantsLabel = getString(Res.string.events_participants_title)
        rule.onNodeWithText(participantsLabel).performClick()
        return AddPersonsScreen()
    }
}
