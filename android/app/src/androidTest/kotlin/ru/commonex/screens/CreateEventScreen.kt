package ru.commonex.screens

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import de.mannodermaus.junit5.compose.ComposeContext
import expenses.shared.feature.events.generated.resources.Res
import expenses.shared.feature.events.generated.resources.events_name_placeholder
import expenses.shared.feature.events.generated.resources.events_participants_title
import org.jetbrains.compose.resources.getString

internal class CreateEventScreen : BaseScreen() {
    context(extension: ComposeContext)
    suspend fun enterEventName(name: String): CreateEventScreen {
        val nameLabel = getString(Res.string.events_name_placeholder)
        extension.onNodeWithText(nameLabel).performTextInput(name)
        return this
    }

    context(extension: ComposeContext)
    fun selectCurrency(currency: String): CreateEventScreen {
        extension.onNodeWithText(currency).performClick()
        return this
    }

    context(extension: ComposeContext)
    suspend fun clickContinueButton(): AddPersonsScreen {
        val participantsLabel = getString(Res.string.events_participants_title)
        extension.onNodeWithText(participantsLabel).performClick()
        return AddPersonsScreen()
    }
}
