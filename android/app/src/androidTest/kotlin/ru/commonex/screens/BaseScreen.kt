package ru.commonex.screens

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import de.mannodermaus.junit5.compose.ComposeContext

internal abstract class BaseScreen {

    context(extension: ComposeContext)
    fun waitForElementWithText(
        text: String,
        timeout: Long = 5000
    ) {
        extension.waitUntilAtLeastOneExists(hasText(text), timeout)
    }

    context(extension: ComposeContext)
    fun assertElementWithTextExists(text: String) {
        extension.onNodeWithText(text).assertIsDisplayed()
    }

    context(extension: ComposeContext)
    fun assertElementsWithTextCount(text: String, count: Int) {
        extension
            .onAllNodesWithText(text, substring = true)
            .assertCountEquals(count)
    }
}