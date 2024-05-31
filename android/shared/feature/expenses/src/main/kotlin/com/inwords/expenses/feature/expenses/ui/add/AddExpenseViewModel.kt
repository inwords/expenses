package com.inwords.expenses.feature.expenses.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.UI
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.expenses.domain.model.Expense
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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

    private val _state = MutableStateFlow(mockAddExpenseScreenUiModel())
    val state: StateFlow<AddExpenseScreenUiModel> = _state

    fun onExpenseTypeClicked(type: ExpenseType) {
        _state.update { state ->
            state.copy(expenseType = type)
        }
    }

    fun onAmountChanged(amount: String) {
        val newAmount = amount.filter { it.isDigit() }.toBigDecimal()
        _state.update { state ->
            state.copy(amount = newAmount)
        }
    }

    fun onCurrencyClicked(currency: Currency) {
        _state.update { state ->
            state.copy(
                currencies = state.currencies.map { currencyUiModel ->
                    currencyUiModel.copy(selected = currency.code == currencyUiModel.currency.code)
                }
            )
        }
    }

    fun onPersonClicked(person: Person) {
        _state.update { state ->
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
        _state.update { state ->
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
        val state = state.value
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
