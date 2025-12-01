package com.inwords.expenses.feature.events.ui.join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.ui.utils.DefaultStringProvider
import com.inwords.expenses.core.ui.utils.StringProvider
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.domain.EventsInteractor.JoinEventResult
import com.inwords.expenses.feature.events.ui.choose_person.ChoosePersonPaneDestination
import com.inwords.expenses.feature.events.ui.join.JoinEventPaneUiModel.EventJoiningState
import expenses.shared.core.ui_design.generated.resources.error_other
import expenses.shared.feature.events.generated.resources.Res
import expenses.shared.feature.events.generated.resources.events_join_error_invalid_credentials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import expenses.shared.core.ui_design.generated.resources.Res as DesignRes

internal class JoinEventViewModel(
    private val navigationController: NavigationController,
    private val eventsInteractor: EventsInteractor,
    private val stringProvider: StringProvider = DefaultStringProvider,
    initialEventId: String,
    initialPinCode: String,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private val eventIdRegex = "[0-9A-HJKMNP-TV-Z]".toRegex()

    private var confirmJob: Job? = null

    private val initialState = JoinEventPaneUiModel(
        eventId = initialEventId.filteredEventId(),
        eventAccessCode = initialPinCode.filteredPinCode(),
        joining = EventJoiningState.None,
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
            value.copy(eventId = eventId.filteredEventId(), joining = EventJoiningState.None)
        }
    }

    fun onEventAccessCodeChanged(eventAccessCode: String) {
        _state.update { value ->
            value.copy(eventAccessCode = eventAccessCode.filteredPinCode(), joining = EventJoiningState.None)
        }
    }

    fun onConfirmClicked() {
        confirmJob?.cancel()

        val state = _state.updateAndGet { currentState ->
            currentState.copy(joining = EventJoiningState.Joining)
        }
        confirmJob = viewModelScope.launch {
            val result = eventsInteractor.joinEvent(
                eventServerId = state.eventId,
                accessCode = state.eventAccessCode
            )

            when (result) {
                is JoinEventResult.NewCurrentEvent -> navigationController.navigateTo(
                    destination = ChoosePersonPaneDestination
                )

                is JoinEventResult.Error -> {
                    val errorMessage = when (result) {
                        JoinEventResult.Error.EventNotFound,
                        JoinEventResult.Error.InvalidAccessCode -> stringProvider.getString(Res.string.events_join_error_invalid_credentials)

                        JoinEventResult.Error.OtherError -> stringProvider.getString(DesignRes.string.error_other)
                    }
                    _state.update { currentState ->
                        currentState.copy(joining = EventJoiningState.Error(errorMessage))
                    }
                }
            }
        }
    }

    fun onNavIconClicked() {
        navigationController.popBackStack()
    }

    private fun String.filteredEventId(): String {
        return this.uppercase().filter { it.toString().matches(eventIdRegex) }
    }

    private fun String.filteredPinCode(): String {
        return this.filter { it.isDigit() }
    }

}
