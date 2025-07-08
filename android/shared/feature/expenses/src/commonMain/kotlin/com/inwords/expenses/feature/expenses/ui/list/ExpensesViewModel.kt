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
import com.inwords.expenses.feature.events.ui.choose_person.ChoosePersonScreenDestination
import com.inwords.expenses.feature.events.ui.create.CreateEventScreenDestination
import com.inwords.expenses.feature.events.ui.join.JoinEventScreenDestination
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.expenses.ui.add.AddExpenseScreenDestination
import com.inwords.expenses.feature.expenses.ui.converter.toUiModel
import com.inwords.expenses.feature.expenses.ui.debts_list.DebtsListScreenDestination
import com.inwords.expenses.feature.expenses.ui.list.ExpensesScreenUiModel.Expenses.DebtorShortUiModel
import com.inwords.expenses.feature.expenses.ui.list.ExpensesScreenUiModel.Expenses.ExpenseUiModel
import com.inwords.expenses.feature.expenses.ui.list.ExpensesScreenUiModel.LocalEvents
import com.inwords.expenses.feature.expenses.ui.list.ExpensesScreenUiModel.LocalEvents.LocalEventUiModel
import com.inwords.expenses.feature.expenses.ui.list.dialog.ExpenseItemDialogDestination
import com.inwords.expenses.feature.expenses.ui.utils.toRoundedString
import com.inwords.expenses.feature.menu.ui.MenuDialogDestination
import com.inwords.expenses.feature.settings.api.SettingsRepository
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class ExpensesViewModel(
    private val navigationController: NavigationController,
    private val eventsInteractor: EventsInteractor,
    private val expensesInteractor: ExpensesInteractor,
    settingsRepository: SettingsRepository,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private var refreshJob: Job? = null
    private var joinEventJob: Job? = null

    private val isRefreshing = MutableStateFlow(false)

    private val _state = MutableStateFlow<SimpleScreenState<ExpensesScreenUiModel>>(SimpleScreenState.Loading)
    val state: StateFlow<SimpleScreenState<ExpensesScreenUiModel>> = _state

    init {
        @OptIn(ExperimentalCoroutinesApi::class)
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
            expensesDetails to currentPersonId
        }.flatMapLatestNoBuffer { (expensesDetails, currentPersonId) ->
            val currentPerson = expensesDetails?.event?.persons?.firstOrNull { it.id == currentPersonId }
            if (expensesDetails == null || currentPerson == null) {
                // local events branch
                return@flatMapLatestNoBuffer eventsInteractor.getEvents().map { events ->
                    if (events.isEmpty()) {
                        SimpleScreenState.Empty
                    } else {
                        SimpleScreenState.Success(
                            LocalEvents(
                                events = events.map { event ->
                                    LocalEventUiModel(
                                        eventId = event.id,
                                        eventName = event.name,
                                    )
                                }.asImmutableListAdapter()
                            )
                        )
                    }
                }
            }

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

            flowOf(
                SimpleScreenState.Success(
                    ExpensesScreenUiModel.Expenses(
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
            )
        }
            .combine(isRefreshing) { state, isRefreshing ->
                if (state is SimpleScreenState.Success) {
                    when (val data = state.data) {
                        is ExpensesScreenUiModel.Expenses -> state.copy(data = data.copy(isRefreshing = isRefreshing))
                        is LocalEvents -> state
                    }
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

    fun onRevertExpenseClick(expense: ExpenseUiModel) {
        navigationController.navigateTo(
            ExpenseItemDialogDestination(
                expenseId = expense.expenseId,
                description = expense.description,
            )
        )
    }

    fun onDebtsDetailsClick() {
        navigationController.navigateTo(DebtsListScreenDestination)
    }

    fun onReplenishmentClick(creditor: DebtorShortUiModel) {
        val state = (_state.value as? SimpleScreenState.Success)?.data ?: return
        val currentPersonId = when (state) {
            is ExpensesScreenUiModel.Expenses -> state.currentPersonId
            is LocalEvents -> return
        }

        navigationController.navigateTo(
            AddExpenseScreenDestination(
                replenishment = AddExpenseScreenDestination.Replenishment(
                    fromPersonId = currentPersonId,
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
        navigationController.navigateTo(JoinEventScreenDestination())
    }

    fun onRefresh() {
        val event = eventsInteractor.currentEvent.value?.event ?: return

        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            isRefreshing.value = true
            expensesInteractor.onRefreshExpensesAsync(event)
            delay(3000)
            isRefreshing.value = false
        }
    }

    fun onJoinLocalEventClick(event: LocalEventUiModel) {
        joinEventJob?.cancel()
        joinEventJob = viewModelScope.launch {
            val joined = eventsInteractor.joinLocalEvent(event.eventId)
            if (joined) {
                navigationController.navigateTo(
                    destination = ChoosePersonScreenDestination
                )
            } else {
                // FIXME: non-fatal error, show a message to the user
            }
        }
    }

}
