package ru.commonex.screens

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import de.mannodermaus.junit5.compose.ComposeContext

internal class AddExpenseScreen : BaseScreen() {

    context(extension: ComposeContext)
    fun enterDescription(description: String): AddExpenseScreen {
        extension.onNodeWithText("Описание").performTextInput(description)
        return this
    }

    context(extension: ComposeContext)
    fun enterAmount(amount: String): AddExpenseScreen {
        extension.onNodeWithText("Общая сумма").performTextInput(amount)
        return this
    }

    context(extension: ComposeContext)
    fun toggleEqualSplit(equalSplit: Boolean): AddExpenseScreen {
        val switch = extension.onNodeWithTag("equal_split_switch")
        val currentIsEqualSplit = switch.fetchSemanticsNode().config[SemanticsProperties.ToggleableState] == ToggleableState.On

        if (currentIsEqualSplit != equalSplit) {
            switch.performClick()
        }
        return this
    }

    context(extension: ComposeContext)
    fun clickConfirm(): ExpensesScreen {
        extension.onNodeWithText("Сохранить").performScrollTo().performClick()
        return ExpensesScreen()
    }
}
