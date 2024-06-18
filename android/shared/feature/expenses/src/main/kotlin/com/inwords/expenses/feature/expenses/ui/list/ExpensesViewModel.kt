package com.inwords.expenses.feature.expenses.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.asImmutableListAdapter
import com.inwords.expenses.core.utils.collectIn
import com.inwords.expenses.core.utils.flatMapLatestNoBuffer
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.expenses.domain.DebtCalculator
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseScreenDestination
import com.inwords.expenses.feature.expenses.ui.converter.toUiModel
import com.inwords.expenses.feature.expenses.ui.list.ExpensesScreenUiModel.DebtorShortUiModel
import com.inwords.expenses.feature.expenses.ui.utils.toRoundedString
import com.inwords.expenses.feature.settings.api.SettingsRepository
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import java.math.BigDecimal

internal class ExpensesViewModel(
    private val navigationController: NavigationController,
    eventsInteractor: EventsInteractor,
    expensesInteractor: ExpensesInteractor,
    settingsRepository: SettingsRepository,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private val _state = MutableStateFlow<SimpleScreenState<ExpensesScreenUiModel>>(SimpleScreenState.Loading)
    val state: StateFlow<SimpleScreenState<ExpensesScreenUiModel>> = _state

    init {
        combine(
            eventsInteractor.currentEvent
                .filterNotNull()
                .flatMapLatestNoBuffer { expensesInteractor.getExpensesDetails(it) },
            settingsRepository.getCurrentPersonId()
        ) { expensesDetails, currentPersonId ->
            val debtCalculator = DebtCalculator(expensesDetails)

            val currentPerson = expensesDetails.expenses.map { it.person }.firstOrNull { it.id == currentPersonId }
            val debtors = if (currentPerson == null) {
                persistentListOf()
            } else {
                debtCalculator.getBarterAccumulatedDebtForPerson(currentPerson).entries
                    .asSequence()
                    .filter { (_, barterAccumulatedDebt) -> barterAccumulatedDebt.barterAmount > BigDecimal.ZERO }
                    .map { (person, barterAccumulatedDebt) ->
                        DebtorShortUiModel(
                            personId = person.id,
                            personName = person.name,
                            amount = barterAccumulatedDebt.barterAmount.toRoundedString()
                        )
                    }
                    .sortedBy { it.amount }
                    .toPersistentList()
            }

            ExpensesScreenUiModel(
                currentPersonName = currentPerson?.name ?: "",
                creditors = debtors,
                expenses = expensesDetails.expenses.map { expense ->
                    expense.toUiModel()
                }.asImmutableListAdapter()
            )
        }
            .collectIn(viewModelScope) {
                _state.value = SimpleScreenState.Success(it)
            }
    }

    fun onAddExpenseClick() {
        navigationController.navigateTo(AddExpenseScreenDestination)
    }

}
