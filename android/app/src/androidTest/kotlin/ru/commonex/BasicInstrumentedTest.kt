package ru.commonex

import androidx.compose.ui.test.ExperimentalTestApi
import de.mannodermaus.junit5.compose.ComposeContext
import de.mannodermaus.junit5.compose.createAndroidComposeExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import ru.commonex.screens.ExpensesScreen
import ru.commonex.screens.LocalEventsScreen
import ru.commonex.ui.MainActivity
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

// .\gradlew.bat :app:connectedAutotestAndroidTest
@OptIn(ExperimentalTestApi::class)
@ExtendWith(ConnectivityExtension::class)
class BasicInstrumentedTest {

    @RegisterExtension
    @ExperimentalTestApi
    private val extension = createAndroidComposeExtension<MainActivity>()

    /**
     * Tests the complete event creation and expenses flow:
     * - Create event with multiple participants
     * - Add expense with equal split (default)
     * - Add expense with non-equal split
     * - Cancel an expense and verify revert
     * - Verify debt calculations
     * - Switch to a different person and verify view updates
     */
    @Test
    fun testBasicNewEventAndExpensesFlow() {
        extension.runTest {
            // Create event and add participants
            val eventName = "UI Test Event"
            val expensesScreen = LocalEventsScreen()
                .clickCreateEvent()
                .enterEventName(eventName)
                .selectCurrency("Euro")
                .clickContinueButton()
                .enterOwnerName("Test User 1")
                .addParticipant("Test User 2")
                .addParticipant("Test User 3")
                .clickContinueButton()
                .waitUntilLoadedEmpty()
                .verifyCurrentPerson(eventName, "Test User 1")

            // Add first expense (equal split - default)
            expensesScreen
                .clickAddExpense()
                .enterDescription("Булка")
                .enterAmount("120")
                .clickConfirm()
                .verifyExpenseAmount("-120")

            // Add second expense with non-equal split
            expensesScreen
                .clickAddExpense()
                .enterDescription("Хот-дог")
                .enterAmount("180")
                .clickEqualSplitSwitch()
                .clickConfirm()
                .verifyExpenseAmount("-180")

            // Cancel first expense
            expensesScreen
                .clickOnExpense("Булка")
                .clickCancelExpense()
                .verifyRevertedExpenseExists("Булка")

            // Verify debts details and go back
            expensesScreen
                .clickDebtDetails()
                .waitUntilLoaded(eventName)
                .verifyDebtAmount("60", "Test User 1", count = 2)
                .goBack()

            // Switch to a different person via menu and verify title updates
            ExpensesScreen()
                .openMenu()
                .chooseParticipant()
                .selectPerson("Test User 2")
                .verifyCurrentPerson(eventName, "Test User 2")
        }
    }

    /**
     * Tests joining an existing remote event:
     * - Enter event ID and access code
     * - Select person from participants list
     * - Verify expenses screen loads
     */
    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun testJoinExistingEvent() {
        extension.runTest {
            LocalEventsScreen()
                .clickJoinEvent()
                .joinEvent("01JYC8BX30EKQYWBRTPKVX6S26", Base64.decode("NTc=").decodeToString() + Base64.decode("NTQ=").decodeToString()) // FIXME: costyl
                .waitUntilLoaded("Test User 2")
                .selectPerson("Test User 2")
                .waitUntilLoadedEmpty()
        }
    }

    /**
     * Tests the complete local event flow:
     * - Create multiple events
     * - Switch between events from the events list (with person selection)
     * - Delete them one by one using "Remove local copy"
     * - Verify snackbar appears after deletion
     * - Verify empty state appears after all events deleted
     */
    @Test
    fun testLocalEventsDeletionFlow() {
        extension.runTest {
            val event1 = "Delete test event 1"
            val event2 = "Delete test event 2"

            createLocalEvent(event1)
            ExpensesScreen().openMenu().openEventsList()
            createLocalEvent(event2)

            // Test switching between events
            ExpensesScreen()
                .openMenu()
                .openEventsList()
                .assertEventExists(event1)
                .assertEventExists(event2)
                // Switch to event1
                .clickEvent(event1)
                .selectPerson("Test User 1")
                .waitUntilLoadedEmpty()
                // Go back and switch to event2
                .openMenu()
                .openEventsList()
                .clickEvent(event2)
                .selectPerson("Test User 1")
                .waitUntilLoadedEmpty()

            // Now test deletion
            ExpensesScreen()
                .openMenu()
                .openEventsList()
                // Delete first event
                .swipeToRevealActions(event1)
                .clickDeleteLocalOnly()
                .assertEventNotExists(event1)
                .assertEventDeletedSnackbar(event1)
                .assertEventExists(event2)
                // Delete second event
                .swipeToRevealActions(event2)
                .clickDeleteLocalOnly()
                .assertEventNotExists(event2)
                // Verify empty state after all events deleted
                .assertCreateJoinDescriptionVisible()
        }
    }

    /**
     * Tests keeping events via both methods:
     * - Keep event using "Keep event" button
     * - Keep event by swiping back
     *
     * @Offline prevents sync to isolate local-only behavior
     */
    @Offline
    @Test
    fun testLocalEventsKeepEvent() {
        extension.runTest {
            val event1 = "Keep via button"
            val event2 = "Keep via swipe back"

            createLocalEvent(event1)
            ExpensesScreen().openMenu().openEventsList()
            createLocalEvent(event2)

            ExpensesScreen()
                .openMenu()
                .openEventsList()
                // Test keeping via button
                .swipeToRevealActions(event1)
                .clickKeepEvent()
                .assertEventExists(event1)
                // Test keeping via swipe back
                .swipeToRevealActions(event2)
                .swipeBack(event2)
                .assertEventExists(event2)
                // Both events should still exist
                .assertEventExists(event1)
                .assertEventExists(event2)
        }
    }

    /**
     * Tests "Delete everywhere" flow for synced events:
     * - Create event (automatically syncs)
     * - Delete using "Delete everywhere" option
     * - Confirm in dialog
     */
    @Test
    fun testSyncedEventDeleteEverywhere() {
        extension.runTest {
            val eventName = "Synced event to delete"

            createLocalEvent(eventName)

            ExpensesScreen()
                .openMenu()
                .openEventsList()
                .swipeToRevealActions(eventName)
                .clickDeleteEverywhere()
                // Confirm deletion in dialog
                .confirmDeletion()
                .assertEventNotExists(eventName)
                .assertCreateJoinDescriptionVisible()
        }
    }

    /**
     * Tests canceling "Delete everywhere" via dialog:
     * - Create synced event
     * - Start delete but cancel in dialog
     * - Verify event still exists
     */
    @Test
    fun testSyncedEventDeleteEverywhereCanceled() {
        extension.runTest {
            val eventName = "Synced event keep"

            createLocalEvent(eventName)

            ExpensesScreen()
                .openMenu()
                .openEventsList()
                .swipeToRevealActions(eventName)
                .clickDeleteEverywhere()
                // Cancel deletion in dialog
                .keepEvent()
                // Event should still exist
                .assertEventExists(eventName)
        }
    }

    /**
     * Tests adding participants to an existing event:
     * - Create event with one participant
     * - Open menu and navigate to "Add participant" screen
     * - Add multiple participants
     * - Verify participants can be selected via Choose Person menu
     * - Verify that an equal expense can be added successfully after new participants are added
     */
    @Test
    fun testAddParticipantsToExistingEvent() {
        extension.runTest {
            val eventName = "Test Event"

            // Create event with one participant
            createLocalEvent(eventName)

            // Add participants via menu
            ExpensesScreen()
                .openMenu()
                .addParticipant()
                .addParticipant("New Test User 1")
                .addParticipant("New Test User 2")
                .clickContinueButton()
                .waitUntilLoadedEmpty()
                .verifyCurrentPerson(eventName, "Test User 1")

            // Verify new participants can be selected
            ExpensesScreen()
                .openMenu()
                .chooseParticipant()
                .waitUntilLoaded("Test User 1")
                .selectPerson("New Test User 1")
                .waitUntilLoadedEmpty()
                .verifyCurrentPerson(eventName, "New Test User 1")

            ExpensesScreen()
                .openMenu()
                .chooseParticipant()
                .waitUntilLoaded("New Test User 1")
                .selectPerson("New Test User 2")
                .waitUntilLoadedEmpty()
                .verifyCurrentPerson(eventName, "New Test User 2")

            // Verify that an equal expense can be added successfully after new participants are added
            ExpensesScreen()
                .clickAddExpense()
                .enterDescription("Test Expense")
                .enterAmount("300")
                .clickConfirm()
                .verifyExpenseAmount("-300")
        }
    }

    private suspend fun ComposeContext.createLocalEvent(eventName: String): ExpensesScreen {
        return LocalEventsScreen()
            .clickCreateEvent()
            .enterEventName(eventName)
            .selectCurrency("Euro")
            .clickContinueButton()
            .enterOwnerName("Test User 1")
            .clickContinueButton()
            .waitUntilLoadedEmpty()
    }

}