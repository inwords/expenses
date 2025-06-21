package com.inwords.expenses.screens

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import de.mannodermaus.junit5.compose.ComposeContext

internal class ExpenseDetailsDialog : BaseScreen() {

    context(extension: ComposeContext)
    fun clickCancelExpense(): ExpensesScreen {
        extension.onNodeWithText("Отменить операцию").performClick()
        return ExpensesScreen()
    }
}
