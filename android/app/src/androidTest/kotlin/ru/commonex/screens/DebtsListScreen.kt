package ru.commonex.screens

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import expenses.shared.feature.expenses.generated.resources.Res
import expenses.shared.feature.expenses.generated.resources.common_back
import org.jetbrains.compose.resources.getString

@OptIn(ExperimentalTestApi::class)
internal class DebtsListScreen : BaseScreen() {

    context(rule: ComposeTestRule)
    suspend fun waitUntilLoaded(eventName: String): DebtsListScreen {
        val backLabel = getString(Res.string.common_back)
        rule.waitUntilAtLeastOneExists(hasContentDescription(backLabel), timeoutMillis = 10000)

        waitForElementWithText(eventName)
        return this
    }

    context(rule: ComposeTestRule)
    fun verifyDebtAmount(amount: String, personName: String, count: Int = 1): DebtsListScreen {
        val debtText = "$amount Euro,  $personName"
        val matcher = hasTestTag("debts_list_debt_button") and hasText(debtText, substring = true)
        rule.waitUntilNodeCount(matcher, count, timeoutMillis = 10000)
        rule.onAllNodes(matcher).assertCountEquals(count)
        return this
    }

    context(rule: ComposeTestRule)
    suspend fun goBack(): ExpensesScreen {
        val backLabel = getString(Res.string.common_back)
        rule.onNodeWithContentDescription(backLabel).performClick()
        return ExpensesScreen()
    }

}
