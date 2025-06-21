package com.inwords.expenses

import androidx.compose.ui.test.ExperimentalTestApi
import com.inwords.expenses.screens.EmptyEventsScreen
import com.inwords.expenses.ui.MainActivity
import de.mannodermaus.junit5.compose.createAndroidComposeExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalTestApi::class)
class BasicInstrumentedTest {

    @RegisterExtension
    @ExperimentalTestApi
    private val extension = createAndroidComposeExtension<MainActivity>()

    @Test
    fun testBasicNewEventAndExpensesFlow() {
        extension.use {
            // Create event and add participants
            val expensesScreen = EmptyEventsScreen()
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
                .verifyExpenseExists("[ОТМЕНА] Булка")

            // Verify debts details
            expensesScreen
                .clickDebtDetails()
                .verifyDebtAmount("60", "Test User 1", 2)
        }
    }

    @Test
    fun testCreateEmptyEvent() {
        extension.use {
            EmptyEventsScreen()
                .clickCreateEvent()
                .enterEventName("UI Test Event")
                .selectCurrency("Euro")
                .clickContinueButton()
                .enterOwnerName("Test User 1")
                .clickContinueButton()
                .waitUntilLoaded()
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun testJoinExistingEvent() {
        extension.use {
            EmptyEventsScreen()
                .clickJoinEvent()
                .joinEvent("62", Base64.decode("NzY=").decodeToString() + Base64.decode("OTU=").decodeToString()) // FIXME: costyl
                .waitUntilLoaded()
                .selectPerson("Test User 2")
                .clickContinueButton()
                .waitUntilLoaded()
        }
    }

}