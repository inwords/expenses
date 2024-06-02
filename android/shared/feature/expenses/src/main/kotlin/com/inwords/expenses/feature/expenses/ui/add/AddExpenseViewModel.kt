package com.inwords.expenses.feature.expenses.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.core.ui.utils.updateIfSuccess
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.UI
import com.inwords.expenses.core.utils.collectIn
import com.inwords.expenses.core.utils.flatMapLatestNoBuffer
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.expenses.domain.model.Expense
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseScreenUiModel.PersonInfoUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import java.math.BigDecimal

internal class AddExpenseViewModel(
    private val navigationController: NavigationController,
    private val eventsInteractor: EventsInteractor,
    private val expensesInteractor: ExpensesInteractor,
    private val homeScreenDestination: Destination,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private val _state = MutableStateFlow<SimpleScreenState<AddExpenseScreenUiModel>>(SimpleScreenState.Empty)
    val state: StateFlow<SimpleScreenState<AddExpenseScreenUiModel>> = _state

    init {
        eventsInteractor.currentEvent
            .filterNotNull() // TODO mvp
            .flatMapLatestNoBuffer { event ->
                eventsInteractor.getEventDetails(event)
            }
            .map { eventDetails ->
                AddExpenseScreenUiModel(
                    amount = null,
                    currencies = eventDetails.currencies.map { currency ->
                        AddExpenseScreenUiModel.CurrencyInfoUiModel(
                            currency = currency,
                            selected = currency.id == eventDetails.primaryCurrency.id
                        )
                    },
                    expenseType = ExpenseType.Spending,
                    persons = eventDetails.persons.map { person ->
                        PersonInfoUiModel(
                            person = person,
                            selected = person.id == eventDetails.primaryPerson.id
                        )
                    },
                    subjectPersons = (eventDetails.persons - eventDetails.primaryPerson).map { person ->
                        PersonInfoUiModel(person = person, selected = true)
                    },
                )
            }
            .collectIn(viewModelScope) { screenUiModel ->
                // TODO mvp - needs error handling and user input preservation (mask)
                _state.value = SimpleScreenState.Success(screenUiModel)
            }
    }

    fun onExpenseTypeClicked(type: ExpenseType) {
        _state.updateIfSuccess { state ->
            state.copy(expenseType = type)
        }
    }

    fun onAmountChanged(amount: String) {
        val newAmount = amount.filter { it.isDigit() }.toBigDecimal()
        _state.updateIfSuccess { state ->
            state.copy(amount = newAmount)
        }
    }

    fun onCurrencyClicked(currency: Currency) {
        _state.updateIfSuccess { state ->
            state.copy(
                currencies = state.currencies.map { currencyUiModel ->
                    currencyUiModel.copy(selected = currency.code == currencyUiModel.currency.code)
                }
            )
        }
    }

    fun onPersonClicked(person: Person) {
        _state.updateIfSuccess { state ->
            state.copy(
                persons = state.persons.map { personUiModel ->
                    personUiModel.copy(selected = personUiModel.person.id == person.id)
                },
                subjectPersons = state.persons.filter { it.person.id != person.id }.map {
                    state.subjectPersons.firstOrNull { personUiModel -> personUiModel.person.id == it.person.id } ?: it
                }
            )
        }
    }

    fun onSubjectPersonClicked(person: Person) {
        _state.updateIfSuccess { state ->
            state.copy(
                subjectPersons = state.subjectPersons.map { personUiModel ->
                    if (personUiModel.person.id == person.id) {
                        personUiModel.copy(selected = !personUiModel.selected)
                    } else {
                        personUiModel
                    }
                }
            )
        }
    }

    fun onConfirmClicked() {
        val state = (state.value as? SimpleScreenState.Success)?.data ?: return
        val selectedCurrency = state.currencies.first { it.selected }.currency
        val selectedPerson = state.persons.first { it.selected }.person
        val expense = Expense(
            expenseId = 0,
            amount = state.amount ?: BigDecimal.ZERO,
            currency = selectedCurrency,
            expenseType = state.expenseType,
            person = selectedPerson,
            subjectPersons = state.subjectPersons.map { it.person },
            timestamp = Clock.System.now(),
            description = "Some description",
        )

        viewModelScope.launch {
            val currentEvent = eventsInteractor.currentEvent.value ?: return@launch // TODO mvp
            expensesInteractor.addExpense(currentEvent, expense)

            withContext(UI) {
                navigationController.popBackStack()
            }
        }
    }

    fun onHomeClicked() {
        navigationController.navigateTo(homeScreenDestination)
    }

}
