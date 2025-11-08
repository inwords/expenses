package com.inwords.expenses.feature.expenses.ui.add

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import com.inwords.expenses.feature.expenses.ui.add.AddExpensePaneUiModel.CurrencyInfoUiModel
import com.inwords.expenses.feature.expenses.ui.add.AddExpensePaneUiModel.ExpenseSplitWithPersonUiModel
import com.inwords.expenses.feature.expenses.ui.add.AddExpensePaneUiModel.PersonInfoUiModel
import com.inwords.expenses.feature.settings.api.SettingsRepository
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class AddExpenseViewModelTest {

    // --- Shared fixtures ---
    private object Fixtures {
        val USD = Currency(id = 100, serverId = null, code = "USD", name = "US Dollar")
        val EUR = Currency(id = 101, serverId = null, code = "EUR", name = "Euro")

        val person1 = Person(id = 1, serverId = "s1", name = "Vasilii")
        val person2 = Person(id = 2, serverId = "s2", name = "Dania")
        val person3 = Person(id = 3, serverId = "s3", name = "Angela")

        val event = Event(
            id = 10L,
            serverId = "ev-10",
            name = "Trip",
            pinCode = "1234",
            primaryCurrencyId = USD.id
        )

        val eventDetails = EventDetails(
            event = event,
            persons = listOf(person1, person2, person3),
            currencies = listOf(USD, EUR),
            primaryCurrency = USD
        )
    }

    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    // --- Controlled backing flows ---
    private val currentEventFlow = MutableStateFlow<EventDetails?>(null)
    private val currentPersonIdFlow = MutableStateFlow<Long?>(Fixtures.person1.id)

    // --- Mocks ---
    private val navigation = mockk<NavigationController>(relaxed = true) {
        justRun { popBackStack() }
    }
    private val eventsInteractor = mockk<EventsInteractor>(relaxed = true) {
        every { currentEvent } returns currentEventFlow
    }
    private val expensesInteractor = mockk<ExpensesInteractor>(relaxed = true)
    private val settingsRepository = mockk<SettingsRepository>(relaxed = true) {
        coEvery { getCurrentPersonId() } returns currentPersonIdFlow
    }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ---------- Tests ----------

    @Test
    fun `emits Loading then Success when event and current person present`() = scope.runTest {
        // Arrange
        val vm = createViewModel()

        // Act & Assert
        vm.state.test {
            awaitLoading()
            currentEventFlow.value = Fixtures.eventDetails

            val ui = awaitSuccess()

            assertEquals("", ui.description)
            assertEquals(ExpenseType.Spending, ui.expenseType)
            assertTrue(ui.currencies.byCode("USD").selected)
            assertTrue(ui.persons.byId(Fixtures.person1.id).selected)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits Error when event is null`() = scope.runTest {
        // Arrange
        val vm = createViewModel()

        // Act & Assert
        vm.state.test {
            awaitLoading()

            // Drive combine to re-evaluate with null event
            runCurrent()
            currentPersonIdFlow.value = Fixtures.person1.id
            currentEventFlow.value = null

            assertIs<SimpleScreenState.Error>(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onCurrencyClicked selects the given currency`() = scope.runTest {
        // Arrange
        currentEventFlow.value = Fixtures.eventDetails
        val vm = createViewModel()

        vm.state.test {
            awaitLoading()
            val initial = awaitSuccess()
            assertTrue(initial.currencies.byCode("USD").selected)

            // Act
            vm.onCurrencyClicked(CurrencyInfoUiModel(currencyName = Fixtures.EUR.name, currencyCode = Fixtures.EUR.code, selected = false))

            // Assert
            val updated = awaitSuccess()
            assertTrue(updated.currencies.byCode("EUR").selected)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onPersonClicked selects payer`() = scope.runTest {
        // Arrange
        currentEventFlow.value = Fixtures.eventDetails
        val vm = createViewModel()

        vm.state.test {
            awaitLoading()
            awaitSuccess()

            // Act
            vm.onPersonClicked(PersonInfoUiModel(Fixtures.person2.id, Fixtures.person2.name, selected = false))

            // Assert
            assertTrue(awaitSuccess().persons.byId(Fixtures.person2.id).selected)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSubjectPersonClicked toggles membership of subject set`() = scope.runTest {
        // Arrange
        currentEventFlow.value = Fixtures.eventDetails
        val vm = createViewModel()

        vm.state.test {
            awaitLoading()
            val initial = awaitSuccess()
            assertTrue(initial.subjectPersons.all { it.selected }) // default: all selected

            // Act — toggle off p3
            vm.onSubjectPersonClicked(PersonInfoUiModel(Fixtures.person3.id, Fixtures.person3.name, selected = true))
            assertTrue(awaitSuccess().subjectPersons.byId(Fixtures.person3.id).selected.not())

            // Act — toggle p3 back on
            vm.onSubjectPersonClicked(PersonInfoUiModel(Fixtures.person3.id, Fixtures.person3.name, selected = false))
            assertTrue(awaitSuccess().subjectPersons.byId(Fixtures.person3.id).selected)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `equal split OFF produces explicit split rows and equalized amounts (scale=2)`() = scope.runTest {
        // Arrange
        currentEventFlow.value = Fixtures.eventDetails
        val vm = createViewModel()

        vm.state.test {
            awaitLoading()
            awaitSuccess()

            // Act
            vm.onEqualSplitChange(false)
            vm.onWholeAmountChanged("10")

            // Assert
            val ui = awaitSuccess()
            assertEquals(false, ui.equalSplit)
            assertEquals(3, ui.split.size)
            assertTrue(ui.split.all { it.amount == "3.33" })

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSplitAmountChanged updates only targeted row`() = scope.runTest {
        // Arrange
        currentEventFlow.value = Fixtures.eventDetails
        val vm = createViewModel()

        vm.state.test {
            awaitLoading()
            awaitSuccess()

            vm.onEqualSplitChange(false)
            vm.onWholeAmountChanged("9") // -> 3,3,3
            val s2 = awaitSuccess()
            assertEquals(listOf("3", "3", "3"), s2.split.map { it.amount })

            // Act — modify p2 only
            val p2Row = s2.split.byPersonId(Fixtures.person2.id)
            vm.onSplitAmountChanged(
                ExpenseSplitWithPersonUiModel(person = p2Row.person, amount = p2Row.amount),
                amount = "4.5"
            )

            // Assert
            val after = awaitSuccess().split
            assertEquals(listOf(Fixtures.person1.id, Fixtures.person2.id, Fixtures.person3.id), after.map { it.person.personId })
            assertEquals("4.5", after.byPersonId(Fixtures.person2.id).amount)
            assertEquals("3", after.byPersonId(Fixtures.person1.id).amount)
            assertEquals("3", after.byPersonId(Fixtures.person3.id).amount)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `confirm with equal split calls addExpenseEqualSplit and navigates back`() = scope.runTest {
        // Arrange
        currentEventFlow.value = Fixtures.eventDetails
        val vm = createViewModel()

        vm.state.test {
            awaitLoading()
            awaitSuccess()
            vm.onEqualSplitChange(true)
            vm.onWholeAmountChanged("12.0")
            awaitSuccess()

            // Act
            vm.onConfirmClicked()
            advanceUntilIdle()

            // Assert
            coVerify(exactly = 1) {
                expensesInteractor.addExpenseEqualSplit(
                    event = Fixtures.event,
                    wholeAmount = "12.0".trim().toBigDecimal(),
                    expenseType = ExpenseType.Spending,
                    description = any(),
                    selectedSubjectPersons = match { it.size == 3 && it.containsAll(listOf(Fixtures.person1, Fixtures.person2, Fixtures.person3)) },
                    selectedCurrency = Fixtures.USD,
                    selectedPerson = Fixtures.person1
                )
            }
            coVerify(exactly = 1) { navigation.popBackStack() }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `confirm with custom split calls addExpenseCustomSplit and navigates back`() = scope.runTest {
        // Arrange
        currentEventFlow.value = Fixtures.eventDetails
        val vm = createViewModel()

        vm.state.test {
            awaitLoading()
            awaitSuccess()
            vm.onEqualSplitChange(false)
            vm.onWholeAmountChanged("9")
            val ui = awaitSuccess()

            // Overwrite two rows
            val p1Row = ui.split.byPersonId(Fixtures.person1.id)
            val p2Row = ui.split.byPersonId(Fixtures.person2.id)
            vm.onSplitAmountChanged(p1Row, "4")
            vm.onSplitAmountChanged(p2Row, "5")
            awaitSuccess()

            // Act
            vm.onConfirmClicked()
            advanceUntilIdle()

            // Assert
            coVerify(exactly = 1) {
                expensesInteractor.addExpenseCustomSplit(
                    event = Fixtures.event,
                    expenseType = ExpenseType.Spending,
                    description = any(),
                    selectedCurrency = Fixtures.USD,
                    selectedPerson = Fixtures.person1,
                    personWithAmountSplit = match { list ->
                        val map = list.associate { it.person to it.amount }
                        map[Fixtures.person1] == "4".toBigDecimal() &&
                            map[Fixtures.person2] == "5".toBigDecimal() &&
                            map.containsKey(Fixtures.person3)
                    }
                )
            }
            coVerify(exactly = 1) { navigation.popBackStack() }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `confirm does nothing when required amounts are missing`() = scope.runTest {
        // Arrange
        currentEventFlow.value = Fixtures.eventDetails
        val vm = createViewModel()

        vm.state.test {
            awaitLoading()
            vm.onEqualSplitChange(true)
            awaitSuccess()

            // Act
            vm.onConfirmClicked()
            advanceUntilIdle()

            // Assert
            coVerify(exactly = 0) { expensesInteractor.addExpenseEqualSplit(any(), any(), any(), any(), any(), any(), any()) }
            coVerify(exactly = 0) { expensesInteractor.addExpenseCustomSplit(any(), any(), any(), any(), any(), any()) }
            coVerify(exactly = 0) { navigation.popBackStack() }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `replenishment pre-fills type currency payer payee description amount and split`() = scope.runTest {
        // Arrange — from p2 → to p3 in EUR amount 7.5
        val repl = AddExpensePaneDestination.Replenishment(
            fromPersonId = Fixtures.person2.id,
            toPersonId = Fixtures.person3.id,
            currencyCode = Fixtures.EUR.code,
            amount = "7.5"
        )
        currentEventFlow.value = Fixtures.eventDetails
        val vm = createViewModel(repl)

        // Act & Assert
        vm.state.test {
            awaitLoading()
            val ui = awaitSuccess()

            assertEquals(ExpenseType.Replenishment, ui.expenseType)
            assertTrue(ui.currencies.byCode("EUR").selected)
            assertTrue(ui.persons.byId(Fixtures.person2.id).selected)
            assertEquals("Возврат от ${Fixtures.person2.name}", ui.description)
            assertEquals(false, ui.equalSplit)
            assertEquals(1, ui.split.size)
            assertEquals(Fixtures.person3.id, ui.split.first().person.personId)
            assertEquals("7.5", ui.split.first().amount)
            assertEquals("7.5", ui.wholeAmount)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onExpenseTypeClicked changes expense type`() = scope.runTest {
        // Arrange
        currentEventFlow.value = Fixtures.eventDetails
        val vm = createViewModel()

        vm.state.test {
            awaitLoading()
            val initial = awaitSuccess()
            assertEquals(ExpenseType.Spending, initial.expenseType)

            // Act
            vm.onExpenseTypeClicked(ExpenseType.Replenishment)

            // Assert
            val updated = awaitSuccess()
            assertEquals(ExpenseType.Replenishment, updated.expenseType)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onDescriptionChanged updates description`() = scope.runTest {
        // Arrange
        currentEventFlow.value = Fixtures.eventDetails
        val vm = createViewModel()

        vm.state.test {
            awaitLoading()
            val initial = awaitSuccess()
            assertEquals("", initial.description)

            // Act
            vm.onDescriptionChanged("Test expense")

            // Assert
            val updated = awaitSuccess()
            assertEquals("Test expense", updated.description)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits Error when current person ID is null`() = scope.runTest {
        // Arrange
        val vm = createViewModel()

        vm.state.test {
            awaitLoading()
            currentEventFlow.value = Fixtures.eventDetails
            currentPersonIdFlow.value = null

            // Assert
            assertIs<SimpleScreenState.Error>(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `currency selection is not changed when selected code not found`() = scope.runTest {
        // Arrange
        currentEventFlow.value = Fixtures.eventDetails
        val vm = createViewModel()

        vm.state.test {
            awaitLoading()
            awaitSuccess()

            // Act - select non-existent currency
            vm.onCurrencyClicked(CurrencyInfoUiModel(currencyName = "Unknown", currencyCode = "XYZ", selected = false))

            // Assert - selection remains unchanged
            expectNoEvents()
        }
    }

    @Test
    fun `subject person selection initializes correctly when null`() = scope.runTest {
        // Arrange
        currentEventFlow.value = Fixtures.eventDetails
        val vm = createViewModel()

        vm.state.test {
            awaitLoading()
            val initial = awaitSuccess()

            // Initially all should be selected (default behavior)
            assertTrue(initial.subjectPersons.all { it.selected })

            // Act - toggle one person off
            vm.onSubjectPersonClicked(PersonInfoUiModel(Fixtures.person1.id, Fixtures.person1.name, selected = true))

            // Assert - person1 should be deselected, others remain selected
            val updated = awaitSuccess()
            assertTrue(updated.subjectPersons.byId(Fixtures.person1.id).selected.not())
            assertTrue(updated.subjectPersons.byId(Fixtures.person2.id).selected)
            assertTrue(updated.subjectPersons.byId(Fixtures.person3.id).selected)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `amount parsing handles invalid input gracefully`() = scope.runTest {
        // Arrange
        currentEventFlow.value = Fixtures.eventDetails
        val vm = createViewModel()

        vm.state.test {
            awaitLoading()
            awaitSuccess()

            // Act - set invalid amount
            vm.onWholeAmountChanged("invalid123")

            // Assert - should store raw value but amount should be null
            val updated = awaitSuccess()
            assertEquals("invalid123", updated.wholeAmount)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `split calculation preserves existing amounts when subject persons change`() = scope.runTest {
        // Arrange
        currentEventFlow.value = Fixtures.eventDetails
        val vm = createViewModel()

        vm.state.test {
            awaitLoading()
            awaitSuccess()

            vm.onEqualSplitChange(false)
            vm.onWholeAmountChanged("9")
            val initial = awaitSuccess()

            // Modify one split amount
            val p1Row = initial.split.byPersonId(Fixtures.person1.id)
            vm.onSplitAmountChanged(p1Row, "5")
            awaitSuccess()

            // Act - remove person2 from subjects
            vm.onSubjectPersonClicked(PersonInfoUiModel(Fixtures.person2.id, Fixtures.person2.name, selected = true))

            // Assert - person1's custom amount should be preserved
            val updated = awaitSuccess()
            assertEquals(2, updated.split.size) // only person1 and person3
            assertEquals("5", updated.split.byPersonId(Fixtures.person1.id).amount)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `confirm handles missing currency gracefully`() = scope.runTest {
        // Arrange - Create scenario where no currency is selected (edge case)
        currentEventFlow.value = Fixtures.eventDetails.copy(currencies = emptyList())
        val vm = createViewModel()

        vm.state.test {
            awaitLoading()
            awaitSuccess()
            vm.onWholeAmountChanged("10")
            awaitSuccess()

            // Act
            vm.onConfirmClicked()
            advanceUntilIdle()

            // Assert - should not call expense creation
            coVerify(exactly = 0) { expensesInteractor.addExpenseEqualSplit(any(), any(), any(), any(), any(), any(), any()) }
            coVerify(exactly = 0) { navigation.popBackStack() }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `confirm handles missing selected person gracefully`() = scope.runTest {
        // This is a defensive test - in normal flow there should always be a selected person
        // but we test the null check in the confirm method
        currentEventFlow.value = Fixtures.eventDetails
        currentPersonIdFlow.value = 999L // Non-existent person ID
        val vm = createViewModel()

        vm.state.test {
            awaitLoading()
            awaitSuccess()
            vm.onWholeAmountChanged("10")
            awaitSuccess()

            // Act
            vm.onConfirmClicked()
            advanceUntilIdle()

            // Assert - should not call expense creation due to missing person
            coVerify(exactly = 0) { expensesInteractor.addExpenseEqualSplit(any(), any(), any(), any(), any(), any(), any()) }
            coVerify(exactly = 0) { navigation.popBackStack() }

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createViewModel(
        replenishment: AddExpensePaneDestination.Replenishment? = null
    ): AddExpenseViewModel {
        return AddExpenseViewModel(
            navigationController = navigation,
            eventsInteractor = eventsInteractor,
            expensesInteractor = expensesInteractor,
            settingsRepository = settingsRepository,
            replenishment = replenishment
        )
    }

    // ---------- Helpers ----------

    private suspend fun ReceiveTurbine<SimpleScreenState<AddExpensePaneUiModel>>.awaitLoading() {
        assertIs<SimpleScreenState.Loading>(awaitItem())
    }

    private suspend fun ReceiveTurbine<SimpleScreenState<AddExpensePaneUiModel>>.awaitSuccess(): AddExpensePaneUiModel {
        val state = awaitItem()
        assertIs<SimpleScreenState.Success<AddExpensePaneUiModel>>(state)
        return state.data
    }

    private fun List<CurrencyInfoUiModel>.byCode(code: String) = first { it.currencyCode == code }

    private fun List<PersonInfoUiModel>.byId(id: Long) = first { it.personId == id }

    private fun List<ExpenseSplitWithPersonUiModel>.byPersonId(id: Long) = first { it.person.personId == id }
}
