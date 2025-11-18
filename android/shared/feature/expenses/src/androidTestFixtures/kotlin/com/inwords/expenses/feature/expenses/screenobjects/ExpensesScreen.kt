package com.inwords.expenses.feature.expenses.screenobjects

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import de.mannodermaus.junit5.compose.ComposeContext
import expenses.shared.feature.expenses.generated.resources.Res
import expenses.shared.feature.expenses.generated.resources.expenses_details
import expenses.shared.feature.expenses.generated.resources.expenses_none
import expenses.shared.feature.expenses.generated.resources.expenses_operation
import expenses.shared.feature.expenses.generated.resources.expenses_revert_description
import org.jetbrains.compose.resources.getString

class ExpensesScreen : BaseScreen() {

    context(extension: ComposeContext)
    suspend fun waitUntilLoaded(): ExpensesScreen {
        val noneLabel = getString(Res.string.expenses_none)
        waitForElementWithText(noneLabel)
        return this
    }

    context(extension: ComposeContext)
    suspend fun clickAddExpense(): AddExpenseScreen {
        val operationLabel = getString(Res.string.expenses_operation)
        extension.onNodeWithText(operationLabel).performClick()
        return AddExpenseScreen()
    }

    context(extension: ComposeContext)
    fun clickOnExpense(description: String): ExpenseDetailsDialog {
        waitForElementWithText(description)
        extension.onNodeWithText(description).performClick()
        return ExpenseDetailsDialog()
    }

    context(extension: ComposeContext)
    suspend fun clickDebtDetails(): DebtsListScreen {
        val detailsLabel = getString(Res.string.expenses_details)
        extension.onNodeWithText(detailsLabel).performClick()
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

    context(extension: ComposeContext)
    suspend fun verifyExpenseReverted(description: String): ExpensesScreen {
        val revertedText = getString(Res.string.expenses_revert_description, description)
        return verifyExpenseExists(revertedText)
    }
}
