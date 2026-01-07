package ru.commonex.screens

import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import de.mannodermaus.junit5.compose.ComposeContext
import expenses.shared.feature.expenses.generated.resources.Res
import expenses.shared.feature.expenses.generated.resources.common_back
import org.jetbrains.compose.resources.getString

internal class DebtsListScreen : BaseScreen() {

    context(extension: ComposeContext)
    suspend fun waitUntilLoaded(eventName: String): DebtsListScreen {
        val backLabel = getString(Res.string.common_back)
        extension.waitUntilAtLeastOneExists(hasContentDescription(backLabel), timeoutMillis = 10000)

        waitForElementWithText(eventName)
        return this
    }

    context(extension: ComposeContext)
    fun verifyDebtAmount(amount: String, personName: String, count: Int = 1): DebtsListScreen {
        val debtText = "$amount Euro,  $personName"
        waitForElementWithText(text = debtText, count = count)
        assertElementsWithTextCount(text = debtText, count = count)
        return this
    }

    context(extension: ComposeContext)
    suspend fun goBack(): ExpensesScreen {
        val backLabel = getString(Res.string.common_back)
        extension.onNodeWithContentDescription(backLabel).performClick()
        return ExpensesScreen()
    }

}
