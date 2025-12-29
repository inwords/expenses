package ru.commonex.screens

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import de.mannodermaus.junit5.compose.ComposeContext
import expenses.shared.feature.expenses.generated.resources.Res
import expenses.shared.feature.expenses.generated.resources.expenses_description
import expenses.shared.feature.expenses.generated.resources.expenses_save
import expenses.shared.feature.expenses.generated.resources.expenses_total_amount
import org.jetbrains.compose.resources.getString

internal class AddExpenseScreen : BaseScreen() {

    context(extension: ComposeContext)
    suspend fun enterDescription(description: String): AddExpenseScreen {
        val descriptionLabel = getString(Res.string.expenses_description)
        extension.onNodeWithText(descriptionLabel).performTextInput(description)
        return this
    }

    context(extension: ComposeContext)
    suspend fun enterAmount(amount: String): AddExpenseScreen {
        val totalAmountLabel = getString(Res.string.expenses_total_amount)
        extension.onNodeWithText(totalAmountLabel).performTextInput(amount)
        return this
    }

    /**
     * Clicks the equal split switch to toggle its state.
     * Default state is ON (equal split), so calling this will turn it OFF.
     */
    context(extension: ComposeContext)
    fun clickEqualSplitSwitch(): AddExpenseScreen {
        extension.onNodeWithTag("equal_split_switch").performClick()
        extension.waitForIdle()
        return this
    }

    context(extension: ComposeContext)
    suspend fun clickConfirm(): ExpensesScreen {
        val saveLabel = getString(Res.string.expenses_save)
        extension.onNodeWithText(saveLabel).performScrollTo().performClick()
        return ExpensesScreen()
    }
}
