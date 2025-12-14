package ru.commonex

import androidx.compose.ui.test.ExperimentalTestApi
import de.mannodermaus.junit5.compose.ComposeContext
import de.mannodermaus.junit5.compose.createAndroidComposeExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import ru.commonex.screens.ExpensesScreen
import ru.commonex.screens.LocalEventsScreen
import ru.commonex.ui.MainActivity
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

// .\gradlew.bat :app:connectedAutotestAndroidTest
@OptIn(ExperimentalTestApi::class)
class BasicInstrumentedTest {

    @RegisterExtension
    @ExperimentalTestApi
    private val extension = createAndroidComposeExtension<MainActivity>()

    @Test
    fun testBasicNewEventAndExpensesFlow() {
        extension.runTest {
            // Create event and add participants
            val expensesScreen = LocalEventsScreen()
                .clickCreateEvent()
                .enterEventName("UI Test Event")
                .selectCurrency("Euro")
                .clickContinueButton()
                .enterOwnerName("Test User 1")
                .addParticipant("Test User 2")
                .addParticipant("Test User 3")
                .clickContinueButton()
                .waitUntilLoaded()

            // Add first expense
            expensesScreen
                .clickAddExpense()
                .enterDescription("Булка")
                .enterAmount("120")
                .clickConfirm()
                .verifyExpenseAmount("-120")

            // Add second expense
            expensesScreen
                .clickAddExpense()
                .enterDescription("Хот-дог")
                .enterAmount("180")
                .clickConfirm()
                .verifyExpenseAmount("-180")

            // Cancel first expense
            expensesScreen
                .clickOnExpense("Булка")
                .clickCancelExpense()
                .verifyRevertedExpenseExists("Булка")

            // Verify debts details
            expensesScreen
                .clickDebtDetails()
                .verifyDebtAmount("60", "Test User 1", 2)
        }
    }

    @Test
    fun testCreateEmptyEvent() {
        extension.runTest {
            createLocalEvent("UI Test Event")
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun testJoinExistingEvent() {
        extension.runTest {
            LocalEventsScreen()
                .clickJoinEvent()
                .joinEvent("01JYC8BX30EKQYWBRTPKVX6S26", Base64.decode("NTc=").decodeToString() + Base64.decode("NTQ=").decodeToString()) // FIXME: costyl
                .waitUntilLoaded("Test User 2")
                .selectPerson("Test User 2")
                .waitUntilLoaded()
        }
    }

    @Test
    fun testLocalEventsDeletion() {
        extension.runTest {
            // Create two local events
            val eventName1 = "Test event 1"
            val eventName2 = "Test event 2"
            createLocalEvent(eventName1)
            ExpensesScreen()
                .openMenu()
                .openEventsList()
            createLocalEvent(eventName2)

            ExpensesScreen()
                // Delete event 1
                .openMenu()
                .openEventsList()
                .swipeToDelete(eventName1)
                .confirmDeletion()
                // Verify event 1 is deleted and event 2 still exists
                .assertEventNotExists(eventName1)
                .assertEventExists(eventName2)
                // Delete event 2
                .swipeToDelete(eventName2)
                .confirmDeletion()
                // Verify event 2 is deleted
                .assertEventNotExists(eventName2)
        }
    }

    @Test
    fun testLocalEventsDeletionKeep() {
        extension.runTest {
            val eventName = "Test event"
            createLocalEvent(eventName)

            ExpensesScreen()
                .openMenu()
                .openEventsList()
                .swipeToDelete(eventName)
                .keepEvent()
                .assertEventExists(eventName)
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
            .waitUntilLoaded()
    }

}