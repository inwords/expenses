package com.inwords.expenses.feature.events.ui.join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.domain.EventsInteractor.JoinEventResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class JoinEventViewModel(
    private val navigationController: NavigationController,
    private val eventsInteractor: EventsInteractor,
    private val expensesScreenDestination: Destination,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private var confirmJob: Job? = null

    private val _state = MutableStateFlow(JoinEventScreenUiModel("", ""))
    val state: StateFlow<JoinEventScreenUiModel> = _state

    fun onEventIdChanged(eventId: String) {
        _state.update { value ->
            value.copy(eventId = eventId.filter { it.isDigit() })
        }
    }

    fun onEventAccessCodeChanged(eventAccessCode: String) {
        _state.update { value ->
            value.copy(eventAccessCode = eventAccessCode.filter { it.isDigit() })
        }
    }

    fun onConfirmClicked() {
        confirmJob?.cancel()
        confirmJob = viewModelScope.launch {
            val state = _state.value
            val result = eventsInteractor.joinEvent(
                eventServerId = state.eventId.toLong(),
                accessCode = state.eventAccessCode
            )
            when (result) {
                is JoinEventResult.NewCurrentEvent -> navigationController.navigateTo(expensesScreenDestination)

                JoinEventResult.InvalidAccessCode -> Unit
                JoinEventResult.EventNotFound -> Unit // TODO mvp
                JoinEventResult.OtherError -> Unit
            }
        }
    }
}