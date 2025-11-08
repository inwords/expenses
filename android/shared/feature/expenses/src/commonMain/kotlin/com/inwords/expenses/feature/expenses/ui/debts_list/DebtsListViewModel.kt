package com.inwords.expenses.feature.expenses.ui.debts_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.asImmutableListAdapter
import com.inwords.expenses.core.utils.asImmutableMap
import com.inwords.expenses.core.utils.flatMapLatestNoBuffer
import com.inwords.expenses.core.utils.stateInWhileSubscribed
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.expenses.ui.add.AddExpensePaneDestination
import com.inwords.expenses.feature.expenses.ui.converter.toUiModel
import com.inwords.expenses.feature.expenses.ui.debts_list.DebtsListPaneUiModel.DebtorShortUiModel
import com.inwords.expenses.feature.expenses.ui.debts_list.DebtsListPaneUiModel.PersonUiModel
import com.inwords.expenses.feature.expenses.ui.utils.toRoundedString
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

internal class DebtsListViewModel(
    private val navigationController: NavigationController,
    eventsInteractor: EventsInteractor,
    expensesInteractor: ExpensesInteractor,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    val state: StateFlow<SimpleScreenState<DebtsListPaneUiModel>> = eventsInteractor.currentEvent
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

            SimpleScreenState.Success(
                data = DebtsListPaneUiModel(
                    eventName = expensesDetails.event.event.name,
                    creditors = creditors.asImmutableMap()
                )
            )
        }
        .stateInWhileSubscribed(viewModelScope, initialValue = SimpleScreenState.Loading)

    fun onReplenishmentClick(debtor: PersonUiModel, creditor: DebtorShortUiModel) {
        navigationController.navigateTo(
            AddExpensePaneDestination(
                replenishment = AddExpensePaneDestination.Replenishment(
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
