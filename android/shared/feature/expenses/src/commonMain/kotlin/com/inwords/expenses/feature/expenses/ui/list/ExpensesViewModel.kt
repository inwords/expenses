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
import com.inwords.expenses.feature.events.ui.create.CreateEventScreenDestination
import com.inwords.expenses.feature.events.ui.join.JoinEventScreenDestination
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseScreenDestination
import com.inwords.expenses.feature.expenses.ui.converter.toUiModel
import com.inwords.expenses.feature.expenses.ui.debts_list.DebtsListScreenDestination
import com.inwords.expenses.feature.expenses.ui.list.ExpensesScreenUiModel.DebtorShortUiModel
import com.inwords.expenses.feature.expenses.ui.utils.toRoundedString
import com.inwords.expenses.feature.menu.ui.MenuDialogDestination
import com.inwords.expenses.feature.settings.api.SettingsRepository
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

internal class ExpensesViewModel(
    private val navigationController: NavigationController,
    private val eventsInteractor: EventsInteractor,
    private val expensesInteractor: ExpensesInteractor,
    settingsRepository: SettingsRepository,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private var refreshJob: Job? = null

    private val isRefreshing = MutableStateFlow(false)

    private val _state = MutableStateFlow<SimpleScreenState<ExpensesScreenUiModel>>(SimpleScreenState.Loading)
    val state: StateFlow<SimpleScreenState<ExpensesScreenUiModel>> = _state

    init {
        combine(
            eventsInteractor.currentEvent
                .flatMapLatestNoBuffer { currentEvent ->
                    if (currentEvent == null) {
                        flowOf(null)
                    } else {
                        expensesInteractor.getExpensesDetails(currentEvent)
                    }
                },
            settingsRepository.getCurrentPersonId()
        ) { expensesDetails, currentPersonId ->
            expensesDetails ?: return@combine SimpleScreenState.Empty

            val currentPerson = expensesDetails.event.persons.firstOrNull { it.id == currentPersonId } ?: return@combine SimpleScreenState.Empty

            val debtors = expensesDetails.debtCalculator.getBarterAccumulatedDebtForPerson(currentPerson)
                .map { (person, barterAccumulatedDebt) ->
                    DebtorShortUiModel(
                        personId = person.id,
                        personName = person.name,
                        currencyCode = barterAccumulatedDebt.currency.code,
                        currencyName = barterAccumulatedDebt.currency.name,
                        amount = barterAccumulatedDebt.barterAmount.toRoundedString()
                    )
                }
                .sortedBy { it.amount }
                .toPersistentList()

            SimpleScreenState.Success(
                ExpensesScreenUiModel(
                    eventName = expensesDetails.event.event.name,
                    currentPersonId = currentPerson.id,
                    currentPersonName = currentPerson.name,
                    creditors = debtors,
                    expenses = expensesDetails.expenses.map { expense ->
                        expense.toUiModel(primaryCurrencyName = expensesDetails.event.primaryCurrency.name)
                    }.asImmutableListAdapter(),
                    isRefreshing = false // TODO costyl
                )
            )
        }
            .combine(isRefreshing) { state, isRefreshing ->
                if (state is SimpleScreenState.Success) {
                    state.copy(data = state.data.copy(isRefreshing = isRefreshing))
                } else {
                    state
                }
            }
            .collectIn(viewModelScope) {
                _state.value = it
            }
    }

    fun onMenuClick() {
        navigationController.navigateTo(MenuDialogDestination)
    }

    fun onAddExpenseClick() {
        navigationController.navigateTo(AddExpenseScreenDestination())
    }

    fun onDebtsDetailsClick() {
        navigationController.navigateTo(DebtsListScreenDestination)
    }

    fun onReplenishmentClick(creditor: DebtorShortUiModel) {
        val state = (_state.value as? SimpleScreenState.Success)?.data ?: return

        navigationController.navigateTo(
            AddExpenseScreenDestination(
                replenishment = AddExpenseScreenDestination.Replenishment(
                    fromPersonId = state.currentPersonId,
                    toPersonId = creditor.personId,
                    currencyCode = creditor.currencyCode,
                    amount = creditor.amount
                )
            )
        )
    }

    fun onCreateEventClick() {
        navigationController.navigateTo(CreateEventScreenDestination)
    }

    fun onJoinEventClick() {
        navigationController.navigateTo(JoinEventScreenDestination)
    }

    fun onRefresh() {
        val event = eventsInteractor.currentEvent.value?.event ?: return

        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            isRefreshing.value = true
            expensesInteractor.onRefreshExpensesAsync(event)
            delay(2000)
            isRefreshing.value = false
        }
    }

}
