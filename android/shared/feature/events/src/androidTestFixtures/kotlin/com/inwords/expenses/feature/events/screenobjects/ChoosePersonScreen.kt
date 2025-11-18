package com.inwords.expenses.feature.events.screenobjects

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.inwords.expenses.feature.expenses.screenobjects.BaseScreen
import com.inwords.expenses.feature.expenses.screenobjects.ExpensesScreen
import de.mannodermaus.junit5.compose.ComposeContext

class ChoosePersonScreen : BaseScreen() {

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
