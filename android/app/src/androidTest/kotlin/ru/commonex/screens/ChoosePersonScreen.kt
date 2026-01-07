package ru.commonex.screens

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import de.mannodermaus.junit5.compose.ComposeContext
import expenses.shared.feature.events.generated.resources.Res
import expenses.shared.feature.events.generated.resources.events_choose_person_title
import org.jetbrains.compose.resources.getString

internal class ChoosePersonScreen : BaseScreen() {

    context(extension: ComposeContext)
    suspend fun waitUntilLoaded(name: String): ChoosePersonScreen {
        val titleLabel = getString(Res.string.events_choose_person_title)
        waitForElementWithText(titleLabel)
        waitForElementWithText(name)
        return this
    }

    context(extension: ComposeContext)
    fun selectPerson(name: String): ExpensesScreen {
        extension.onNodeWithText(name).performClick()
        return ExpensesScreen()
    }

}
