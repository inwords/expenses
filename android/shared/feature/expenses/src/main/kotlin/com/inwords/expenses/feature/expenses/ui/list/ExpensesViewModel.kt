package com.inwords.expenses.feature.expenses.ui.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.collectIn
import com.inwords.expenses.core.utils.flatMapLatestNoBuffer
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.expenses.domain.model.Expense
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseScreenDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import java.math.BigDecimal
import java.math.RoundingMode

internal class ExpensesViewModel(
    private val navigationController: NavigationController,
    eventsInteractor: EventsInteractor,
    expensesInteractor: ExpensesInteractor,
    private val homeScreenDestination: Destination,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private val _state = MutableStateFlow<SimpleScreenState<ExpensesScreenUiModel>>(SimpleScreenState.Loading)
    val state: StateFlow<SimpleScreenState<ExpensesScreenUiModel>> = _state

    init {
        eventsInteractor.currentEvent
            .filterNotNull()
            .flatMapLatestNoBuffer { expensesInteractor.getExpensesDetails(it) }
            .collectIn(viewModelScope) { expensesDetails ->
                val expensesForPerson = hashMapOf<Person, MutableList<Expense>>()
                expensesDetails.expenses.forEach { expense ->
                    expensesForPerson.getOrPut(expense.person) { mutableListOf() }.add(expense)
                    expense.subjectPersons.forEach { subjectPerson ->
                        expensesForPerson.getOrPut(subjectPerson) { mutableListOf() }.add(expense)
                    }
                }

                val sumForPerson = expensesForPerson.mapValues { entry ->
                    entry.value.sumOf {
                        if (it.person == entry.key) {
                            it.amount
                        } else {
                            it.amount
                                .negate()
                                .divide(BigDecimal(it.subjectPersons.size), 10, RoundingMode.HALF_UP)
                        }
                    }
                }
                val person = expensesDetails.event.persons.firstOrNull { it.name == "Василий" }
                if (person != null) {
                    val checkForVasilii = expensesForPerson[person]?.forEach {
                        // FIXME
                    }
                }

                Log.e("ExpensesViewModel", "init: $sumForPerson")

                _state.value = SimpleScreenState.Success(
                    ExpensesScreenUiModel(
                        expenses = expensesDetails.expenses.map { expense ->
                            ExpensesScreenUiModel.ExpenseUiModel(expense)
                        }
                    )
                )
            }
    }

    fun onAddExpenseClick() {
        navigationController.navigateTo(AddExpenseScreenDestination)
    }

    fun onHomeClick() {
        navigationController.navigateTo(homeScreenDestination)
    }

}