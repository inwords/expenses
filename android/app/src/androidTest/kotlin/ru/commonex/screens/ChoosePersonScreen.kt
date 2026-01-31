package ru.commonex.screens

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import expenses.shared.feature.events.generated.resources.Res
import expenses.shared.feature.events.generated.resources.events_choose_person_title
import org.jetbrains.compose.resources.getString

internal class ChoosePersonScreen : BaseScreen() {

    context(rule: ComposeTestRule)
    suspend fun waitUntilLoaded(name: String): ChoosePersonScreen {
        val titleLabel = getString(Res.string.events_choose_person_title)
        waitForElementWithText(titleLabel)
        waitForElementWithText(name)
        return this
    }

    context(rule: ComposeTestRule)
    fun selectPerson(name: String): ExpensesScreen {
        rule.onNodeWithText(name).performClick()
        return ExpensesScreen()
    }

}
