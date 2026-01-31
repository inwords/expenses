package ru.commonex

import androidx.compose.ui.test.junit4.ComposeTestRule
import kotlinx.coroutines.runBlocking

/**
 * Runs a test block within the context of the [ComposeTestRule].
 *
 * This utility simplifies test code by:
 * 1. Wrapping the block in [runBlocking] for coroutine support
 * 2. Providing the [ComposeTestRule] as a context receiver to the block
 *
 * Note: For instrumented tests, [runBlocking] is appropriate because:
 * - UI operations must run on the main thread
 * - The device/emulator runs in real-time (no virtual time control)
 * - TestScope/StandardTestDispatcher conflicts with Compose's threading model
 *
 * Usage:
 * ```kotlin
 * private val composeRule = createAndroidComposeRule<MainActivity>()
 *
 * @Test
 * fun testSomeFlow() = composeRule.runTest {
 *     LocalEventsScreen()
 *         .clickCreateEvent()
 *         .enterEventName("Test")
 * }
 * ```
 */
internal inline fun ComposeTestRule.runTest(crossinline block: suspend context(ComposeTestRule) () -> Unit) {
    runBlocking {
        block(this@runTest)
    }
}
