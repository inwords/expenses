package com.inwords.expenses.screens

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import de.mannodermaus.junit5.compose.ComposeContext

internal class CreateEventScreen : BaseScreen() {
    context(extension: ComposeContext)
    fun enterEventName(name: String): CreateEventScreen {
        extension.onNodeWithText("Название события").performTextInput(name)
        return this
    }

    context(extension: ComposeContext)
    fun selectCurrency(currency: String): CreateEventScreen {
        extension.onNodeWithText(currency).performClick()
        return this
    }

    context(extension: ComposeContext)
    fun clickContinueButton(): AddPersonsScreen {
        extension.onNodeWithText("Участники").performClick()
        return AddPersonsScreen()
    }
}
