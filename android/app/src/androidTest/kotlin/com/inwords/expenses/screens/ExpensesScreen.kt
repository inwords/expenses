package com.inwords.expenses.screens

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import de.mannodermaus.junit5.compose.ComposeContext

internal class ExpensesScreen : BaseScreen() {
    context(extension: ComposeContext)
    fun waitUntilLoaded(): ExpensesScreen {
        waitForElementWithText("Отсутствуют")
        return this
    }

    context(extension: ComposeContext)
    fun clickAddExpense(): AddExpenseScreen {
        extension.onNodeWithText("Операция").performClick()
        return AddExpenseScreen()
    }

    context(extension: ComposeContext)
    fun clickOnExpense(description: String): ExpenseDetailsDialog {
        waitForElementWithText(description)
        extension.onNodeWithText(description).performClick()
        return ExpenseDetailsDialog()
    }

    context(extension: ComposeContext)
    fun clickDebtDetails(): DebtsListScreen {
        extension.onNodeWithText("детализация").performClick()
        return DebtsListScreen()
    }

    context(extension: ComposeContext)
    fun verifyExpenseAmount(amount: String): ExpensesScreen {
        waitForElementWithText(amount)
        assertElementWithTextExists(amount)
        return this
    }

    context(extension: ComposeContext)
    fun verifyExpenseDescription(description: String): ExpensesScreen {
        waitForElementWithText(description)
        assertElementWithTextExists(description)
        return this
    }

    context(extension: ComposeContext)
    fun verifyExpenseExists(description: String): ExpensesScreen {
        waitForElementWithText(description)
        assertElementWithTextExists(description)
        return this
    }
}
