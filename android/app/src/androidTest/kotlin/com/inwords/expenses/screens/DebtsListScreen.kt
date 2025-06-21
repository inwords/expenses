package com.inwords.expenses.screens

import de.mannodermaus.junit5.compose.ComposeContext

internal class DebtsListScreen : BaseScreen() {

    context(extension: ComposeContext)
    fun verifyDebtAmount(amount: String, personName: String, count: Int = 1): DebtsListScreen {
        val debtText = "$amount Euro,  $personName"
        waitForElementWithText(debtText)
        assertElementsWithTextCount(debtText, count)
        return this
    }

}
