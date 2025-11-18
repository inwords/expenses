package com.inwords.expenses.feature.expenses.screenobjects

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import de.mannodermaus.junit5.compose.ComposeContext
import expenses.shared.feature.expenses.generated.resources.Res
import expenses.shared.feature.expenses.generated.resources.expenses_revert_operation
import org.jetbrains.compose.resources.getString

class ExpenseDetailsDialog : BaseScreen() {

    context(extension: ComposeContext)
    suspend fun clickCancelExpense(): ExpensesScreen {
        val revertLabel = getString(Res.string.expenses_revert_operation)
        extension.onNodeWithText(revertLabel).performClick()
        return ExpensesScreen()
    }
}
