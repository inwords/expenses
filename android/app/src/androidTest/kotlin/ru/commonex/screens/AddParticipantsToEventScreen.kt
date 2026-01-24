package ru.commonex.screens

import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import de.mannodermaus.junit5.compose.ComposeContext
import expenses.shared.feature.events.generated.resources.Res
import expenses.shared.feature.events.generated.resources.events_add_participant
import expenses.shared.feature.events.generated.resources.events_continue_button
import expenses.shared.feature.events.generated.resources.events_person_name_placeholder
import org.jetbrains.compose.resources.getString

internal class AddParticipantsToEventScreen : BaseScreen() {
    context(extension: ComposeContext)
    suspend fun addParticipant(name: String): AddParticipantsToEventScreen {
        val addParticipantLabel = getString(Res.string.events_add_participant)
        extension.onNodeWithText(addParticipantLabel).performClick()

        // Fill the last field (the newly added one)
        val nameLabel = getString(Res.string.events_person_name_placeholder)
        extension.onAllNodesWithText(nameLabel).onLast().performTextInput(name)
        return this
    }

    context(extension: ComposeContext)
    suspend fun clickContinueButton(): ExpensesScreen {
        val continueLabel = getString(Res.string.events_continue_button)
        extension.onNodeWithText(continueLabel).performClick()
        return ExpensesScreen()
    }
}
