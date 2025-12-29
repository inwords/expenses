package com.inwords.expenses.feature.expenses.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.asImmutableListAdapter
import com.inwords.expenses.core.utils.debounceAfterInitial
import com.inwords.expenses.core.utils.flatMapLatestNoBuffer
import com.inwords.expenses.core.utils.stateInWhileSubscribed
import com.inwords.expenses.feature.events.api.EventDeletionStateManager
import com.inwords.expenses.feature.events.api.EventDeletionStateManager.EventDeletionState
import com.inwords.expenses.feature.events.domain.DeleteEventUseCase
import com.inwords.expenses.feature.events.domain.GetCurrentEventStateUseCase
import com.inwords.expenses.feature.events.domain.GetEventsUseCase
import com.inwords.expenses.feature.events.domain.JoinEventUseCase
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.ui.choose_person.ChoosePersonPaneDestination
import com.inwords.expenses.feature.events.ui.create.CreateEventPaneDestination
import com.inwords.expenses.feature.events.ui.dialog.delete.DeleteEventDialogDestination
import com.inwords.expenses.feature.events.ui.join.JoinEventPaneDestination
import com.inwords.expenses.feature.events.ui.local.LocalEventsUiModel
import com.inwords.expenses.feature.events.ui.local.LocalEventsUiModel.LocalEventUiModel
import com.inwords.expenses.feature.expenses.domain.ExpensesInteractor
import com.inwords.expenses.feature.expenses.ui.add.AddExpensePaneDestination
import com.inwords.expenses.feature.expenses.ui.common.DebtShortUiModel
import com.inwords.expenses.feature.expenses.ui.converter.toUiModel
import com.inwords.expenses.feature.expenses.ui.debts_list.DebtsListPaneDestination
import com.inwords.expenses.feature.expenses.ui.list.ExpensesPaneUiModel.Expenses.ExpenseUiModel
import com.inwords.expenses.feature.expenses.ui.list.ExpensesPaneUiModel.LocalEvents
import com.inwords.expenses.feature.expenses.ui.list.dialog.item.ExpenseItemDialogDestination
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
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

internal class ExpensesViewModel(
    private val navigationController: NavigationController,
    private val getCurrentEventStateUseCase: GetCurrentEventStateUseCase,
    private val eventDeletionStateManager: EventDeletionStateManager,
    private val getEventsUseCase: GetEventsUseCase,
    private val joinEventUseCase: JoinEventUseCase,
    private val deleteEventUseCase: DeleteEventUseCase,
    private val expensesInteractor: ExpensesInteractor,
    settingsRepository: SettingsRepository,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private var refreshJob: Job? = null
    private var joinEventJob: Job? = null
    private var recentlyRemovedEventJob: Job? = null

    private val isRefreshing = MutableStateFlow(false)
    private val recentlyRemovedEventName = MutableStateFlow<String?>(null)

    private val localEventsState = flow<SimpleScreenState<ExpensesPaneUiModel>> {
        var previousEvents = emptyList<Event>()
        combine(
            getEventsUseCase.getEvents(),
            eventDeletionStateManager.eventsDeletionState,
            recentlyRemovedEventName,
        ) { events, eventsDeletionState, recentlyRemovedEventName ->
            val result = if (events.isEmpty()) {
                SimpleScreenState.Empty
            } else {
                handleEventRemovalDetection(previousEvents, events)
                SimpleScreenState.Success(
                    data = LocalEvents(
                        localEvents = LocalEventsUiModel(
                            events = events.map { event ->
                                LocalEventUiModel(
                                    eventId = event.id,
                                    eventName = event.name,
                                    isSynced = event.serverId != null,
                                    deletionState = eventsDeletionState[event.id] ?: EventDeletionState.None
                                )
                            }.asImmutableListAdapter(),
                            recentlyRemovedEventName = recentlyRemovedEventName,
                        )
                    )
                )
            }
            previousEvents = events
            result
        }.let { emitAll(it) }
    }

    val state: StateFlow<SimpleScreenState<ExpensesPaneUiModel>> = combine(
        getCurrentEventStateUseCase.currentEvent
            .flatMapLatestNoBuffer { currentEvent ->
                if (currentEvent == null) {
                    flowOf(null)
                } else {
                    expensesInteractor.getExpensesDetails(currentEvent)
                        .debounceAfterInitial(500.milliseconds)
                }
            },
        settingsRepository.getCurrentPersonId()
    ) { expensesDetails, currentPersonId ->
        expensesDetails to currentPersonId
    }.flatMapLatestNoBuffer { (expensesDetails, currentPersonId) ->
        val currentPerson = expensesDetails?.event?.persons?.firstOrNull { it.id == currentPersonId }
        if (expensesDetails == null || currentPerson == null) {
            // local events branch
            return@flatMapLatestNoBuffer localEventsState
        }

        val debts = expensesDetails.debtCalculator.getBarterAccumulatedDebtForPerson(currentPerson)
            .map { (person, barterAccumulatedDebt) ->
                DebtShortUiModel(
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
                ExpensesPaneUiModel.Expenses(
                    eventName = expensesDetails.event.event.name,
                    currentPersonId = currentPerson.id,
                    currentPersonName = currentPerson.name,
                    debts = debts,
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
                    is ExpensesPaneUiModel.Expenses -> state.copy(data = data.copy(isRefreshing = isRefreshing))
                    is LocalEvents -> state
                }
            } else {
                state
            }
        }
        .stateInWhileSubscribed(
            scope = viewModelScope,
            initialValue = SimpleScreenState.Loading,
            replayExpirationMillis = 3000,
        )

    fun onMenuClick() {
        navigationController.navigateTo(MenuDialogDestination)
    }

    fun onAddExpenseClick() {
        navigationController.navigateTo(AddExpensePaneDestination())
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
        navigationController.navigateTo(DebtsListPaneDestination)
    }

    fun onReplenishmentClick(creditor: DebtShortUiModel) {
        val state = (state.value as? SimpleScreenState.Success)?.data ?: return
        val currentPersonId = when (state) {
            is ExpensesPaneUiModel.Expenses -> state.currentPersonId
            is LocalEvents -> return
        }

        navigationController.navigateTo(
            AddExpensePaneDestination(
                replenishment = AddExpensePaneDestination.Replenishment(
                    fromPersonId = currentPersonId,
                    toPersonId = creditor.personId,
                    currencyCode = creditor.currencyCode,
                    amount = creditor.amount
                )
            )
        )
    }

    fun onCreateEventClick() {
        navigationController.navigateTo(CreateEventPaneDestination)
    }

    fun onJoinEventClick() {
        navigationController.navigateTo(JoinEventPaneDestination())
    }

    fun onRefresh() {
        val event = getCurrentEventStateUseCase.currentEvent.value?.event ?: return

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
            val joined = joinEventUseCase.joinLocalEvent(event.eventId)
            if (joined) {
                navigationController.navigateTo(
                    destination = ChoosePersonPaneDestination
                )
            } else {
                // FIXME: non-fatal error, show a message to the user
            }
        }
    }

    fun onDeleteEventClick(event: LocalEventUiModel) {
        navigationController.navigateTo(
            DeleteEventDialogDestination(
                eventId = event.eventId,
                eventName = event.eventName
            )
        )
    }

    fun onDeleteOnlyLocalEventClick(event: LocalEventUiModel) {
        viewModelScope.launch {
            deleteEventUseCase.deleteLocalEvent(event.eventId)
        }
    }

    fun onKeepLocalEventClick(event: LocalEventUiModel) {
        eventDeletionStateManager.clearEventDeletionState(event.eventId)
    }

    private fun handleEventRemovalDetection(
        previousEvents: List<Event>,
        newEvents: List<Event>
    ) {
        if (previousEvents.size - 1 == newEvents.size) {
            for (i in previousEvents.indices) {
                if (i >= newEvents.size || previousEvents[i].id != newEvents[i].id) {
                    recentlyRemovedEventName.value = null
                    recentlyRemovedEventJob?.cancel()

                    val removedEvent = previousEvents[i]

                    recentlyRemovedEventJob = viewModelScope.launch {
                        recentlyRemovedEventName.value = removedEvent.name
                        delay(3000)
                        recentlyRemovedEventName.value = null
                    }
                    break
                }
            }
        }
    }

}
