package com.inwords.expenses.feature.events.ui.join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.domain.EventsInteractor.JoinEventResult
import com.inwords.expenses.feature.events.ui.choose_person.ChoosePersonPaneDestination
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
    initialEventId: String,
    initialPinCode: String,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private val eventIdRegex = "[0-9A-HJKMNP-TV-Z]".toRegex()

    private var confirmJob: Job? = null

    private val initialState = JoinEventPaneUiModel(
        eventId = initialEventId.filteredEventId(),
        eventAccessCode = initialPinCode.filteredPinCode(),
    )
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<JoinEventPaneUiModel> = _state

    init {
        if (initialState.eventId.isNotBlank() && initialState.eventAccessCode.isNotBlank()) {
            onConfirmClicked()
        }
    }

    fun onEventIdChanged(eventId: String) {
        _state.update { value ->
            value.copy(eventId = eventId.filteredEventId())
        }
    }

    fun onEventAccessCodeChanged(eventAccessCode: String) {
        _state.update { value ->
            value.copy(eventAccessCode = eventAccessCode.filteredPinCode())
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
                    destination = ChoosePersonPaneDestination
                )

                JoinEventResult.InvalidAccessCode -> Unit
                JoinEventResult.EventNotFound -> Unit // TODO mvp
                JoinEventResult.OtherError -> Unit
            }
        }
    }

    fun onNavIconClicked() {
        navigationController.popBackStack()
    }

    private fun String.filteredEventId(): String {
        return this.filter { it.toString().matches(eventIdRegex) }
    }

    private fun String.filteredPinCode(): String {
        return this.filter { it.isDigit() }
    }

}
