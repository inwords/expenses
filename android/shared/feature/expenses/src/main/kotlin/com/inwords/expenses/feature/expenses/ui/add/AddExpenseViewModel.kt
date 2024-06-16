package com.inwords.expenses.feature.expenses.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.inwords.expenses.feature.expenses.domain.model.ExpenseSplitWithPerson
import com.inwords.expenses.feature.expenses.domain.model.ExpenseType
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseScreenUiModel.PersonInfoUiModel
import com.inwords.expenses.feature.settings.api.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import java.math.BigDecimal
import java.math.RoundingMode

internal class AddExpenseViewModel(
    private val navigationController: NavigationController,
    private val eventsInteractor: EventsInteractor,
    private val expensesInteractor: ExpensesInteractor,
    settingsRepository: SettingsRepository,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private val _state = MutableStateFlow<SimpleScreenState<AddExpenseScreenUiModel>>(SimpleScreenState.Empty)
    val state: StateFlow<SimpleScreenState<AddExpenseScreenUiModel>> = _state

    init {
        combine(
            eventsInteractor.currentEvent
                .filterNotNull() // TODO mvp
                .flatMapLatestNoBuffer { event ->
                    eventsInteractor.getEventDetails(event)
                },
            settingsRepository.getCurrentPersonId()
        ) { eventDetails, currentPersonId ->
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
                        selected = person.id == currentPersonId
                    )
                },
                subjectPersons = eventDetails.persons.map { person ->
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
        val newAmount = amount.filter { it.isDigit() }.takeIf { it.isNotEmpty() }?.toBigDecimal()
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
                subjectPersons = state.persons.map {
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

        val amount = state.amount ?: BigDecimal.ZERO
        val expense = Expense(
            expenseId = 0,
            currency = selectedCurrency,
            expenseType = state.expenseType,
            person = selectedPerson,
            subjecExpenseSplitWithPersons = state.subjectPersons.map {
                ExpenseSplitWithPerson(
                    expenseSplitId = 0,
                    expenseId = 0,
                    person = it.person,
                    amount = amount.divide(state.subjectPersons.count { it.selected }.coerceAtLeast(1).toBigDecimal(), 3, RoundingMode.HALF_EVEN) // FIXME
                )
            },
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

}
