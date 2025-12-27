package ru.commonex.screens

import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import de.mannodermaus.junit5.compose.ComposeContext
import expenses.shared.feature.expenses.generated.resources.Res
import expenses.shared.feature.expenses.generated.resources.common_back
import org.jetbrains.compose.resources.getString

internal class DebtsListScreen : BaseScreen() {

    context(extension: ComposeContext)
    fun verifyDebtAmount(amount: String, personName: String, count: Int = 1): DebtsListScreen {
        val debtText = "$amount Euro,  $personName"
        waitForElementWithText(debtText)
        assertElementsWithTextCount(debtText, count)
        return this
    }

    context(extension: ComposeContext)
    suspend fun goBack(): ExpensesScreen {
        val backLabel = getString(Res.string.common_back)
        extension.onNodeWithContentDescription(backLabel).performClick()
        return ExpensesScreen()
    }

}
