package ru.commonex.screens

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import de.mannodermaus.junit5.compose.ComposeContext

internal class ChoosePersonScreen : BaseScreen() {

    context(extension: ComposeContext)
    fun waitUntilLoaded(name: String): ChoosePersonScreen {
        waitForElementWithText(name)
        return this
    }

    context(extension: ComposeContext)
    fun selectPerson(name: String): ExpensesScreen {
        extension.onNodeWithText(name).performClick()
        return ExpensesScreen()
    }

}
