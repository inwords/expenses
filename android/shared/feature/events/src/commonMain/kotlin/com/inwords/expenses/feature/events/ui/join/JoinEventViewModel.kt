package com.inwords.expenses.feature.events.ui.join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.domain.EventsInteractor.JoinEventResult
import com.inwords.expenses.feature.events.ui.choose_person.ChoosePersonScreenDestination
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
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private val eventIdRegex = "[0-9A-HJKMNP-TV-Z]".toRegex()

    private var confirmJob: Job? = null

    private val _state = MutableStateFlow(JoinEventScreenUiModel("", ""))
    val state: StateFlow<JoinEventScreenUiModel> = _state

    fun onEventIdChanged(eventId: String) {
        _state.update { value ->
            value.copy(eventId = eventId.filter { it.toString().matches(eventIdRegex) })
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
                eventServerId = state.eventId,
                accessCode = state.eventAccessCode
            )
            when (result) {
                is JoinEventResult.NewCurrentEvent -> navigationController.navigateTo(
                    destination = ChoosePersonScreenDestination
                )

                JoinEventResult.InvalidAccessCode -> Unit
                JoinEventResult.EventNotFound -> Unit // TODO mvp
                JoinEventResult.OtherError -> Unit
            }
        }
    }
}