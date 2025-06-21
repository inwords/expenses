package com.inwords.expenses.screens

import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import de.mannodermaus.junit5.compose.ComposeContext

internal class AddPersonsScreen : BaseScreen() {
    context(extension: ComposeContext)
    fun enterOwnerName(name: String): AddPersonsScreen {
        extension.onNodeWithText("Имя").performTextInput(name)
        return this
    }

    context(extension: ComposeContext)
    fun addParticipant(name: String): AddPersonsScreen {
        extension.onNodeWithText("Добавить участника").performClick()
        extension.onAllNodesWithText("Имя").onLast().performTextInput(name)
        return this
    }

    context(extension: ComposeContext)
    fun clickContinueButton(): ExpensesScreen {
        extension.onNodeWithText("К событию").performClick()
        return ExpensesScreen()
    }
}
