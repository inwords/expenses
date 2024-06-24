package com.inwords.expenses.feature.expenses.ui.debts_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.asImmutableListAdapter
import com.inwords.expenses.core.utils.asImmutableMap
import com.inwords.expenses.core.utils.collectIn
import com.inwords.expenses.core.utils.flatMapLatestNoBuffer
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseScreenDestination
import com.inwords.expenses.feature.expenses.ui.converter.toUiModel
import com.inwords.expenses.feature.expenses.ui.debts_list.DebtsListScreenUiModel.DebtorShortUiModel
import com.inwords.expenses.feature.expenses.ui.debts_list.DebtsListScreenUiModel.PersonUiModel
import com.inwords.expenses.feature.expenses.ui.utils.toRoundedString
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

internal class DebtsListViewModel(
    private val navigationController: NavigationController,
    eventsInteractor: EventsInteractor,
    expensesInteractor: ExpensesInteractor,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private val _state = MutableStateFlow<SimpleScreenState<DebtsListScreenUiModel>>(SimpleScreenState.Loading)
    val state: StateFlow<SimpleScreenState<DebtsListScreenUiModel>> = _state

    init {
        eventsInteractor.currentEvent
            .filterNotNull() // TODO mvp
            .flatMapLatestNoBuffer { expensesInteractor.getExpensesDetails(it) }
            .map { expensesDetails ->
                val creditors = hashMapOf<PersonUiModel, ImmutableList<DebtorShortUiModel>>()
                expensesDetails.debtCalculator.barterAccumulatedDebts.forEach { (debtor, barterAccumulatedDebts) ->
                    creditors[debtor.toUiModel()] = barterAccumulatedDebts.map { (creditor, barterAccumulatedDebt) ->
                        DebtorShortUiModel(
                            person = creditor.toUiModel(),
                            currencyCode = barterAccumulatedDebt.currency.code,
                            currencyName = barterAccumulatedDebt.currency.name,
                            amount = barterAccumulatedDebt.barterAmount.toRoundedString()
                        )
                    }.asImmutableListAdapter()
                }

                DebtsListScreenUiModel(
                    eventName = expensesDetails.event.event.name,
                    creditors = creditors.asImmutableMap()
                )
            }
            .collectIn(viewModelScope) {
                _state.value = SimpleScreenState.Success(it)
            }
    }

    fun onReplenishmentClick(debtor: PersonUiModel, creditor: DebtorShortUiModel) {
        navigationController.navigateTo(
            AddExpenseScreenDestination(
                replenishment = AddExpenseScreenDestination.Replenishment(
                    fromPersonId = debtor.personId,
                    toPersonId = creditor.person.personId,
                    currencyCode = creditor.currencyCode,
                    amount = creditor.amount
                )
            )
        )
    }

    fun onCloseClick() {
        navigationController.popBackStack()
    }

}
