package ru.commonex.screens

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import expenses.shared.feature.expenses.generated.resources.Res
import expenses.shared.feature.expenses.generated.resources.expenses_revert_operation
import org.jetbrains.compose.resources.getString

internal class ExpenseDetailsDialog : BaseScreen() {

    context(rule: ComposeTestRule)
    suspend fun clickCancelExpense(): ExpensesScreen {
        val revertLabel = getString(Res.string.expenses_revert_operation)
        rule.onNodeWithText(revertLabel).performClick()
        return ExpensesScreen()
    }
}
