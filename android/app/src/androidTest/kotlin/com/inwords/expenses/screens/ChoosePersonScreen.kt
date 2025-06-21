package com.inwords.expenses.screens

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import de.mannodermaus.junit5.compose.ComposeContext

internal class ChoosePersonScreen : BaseScreen() {

    context(extension: ComposeContext)
    fun waitUntilLoaded(): ChoosePersonScreen {
        waitForElementWithText("Продолжить")
        return this
    }

    context(extension: ComposeContext)
    fun selectPerson(name: String): ChoosePersonScreen {
        extension.onNodeWithText(name).performClick()
        return this
    }

    context(extension: ComposeContext)
    fun clickContinueButton(): ExpensesScreen {
        extension.onNodeWithText("Продолжить").performClick()
        return ExpensesScreen()
    }

}
