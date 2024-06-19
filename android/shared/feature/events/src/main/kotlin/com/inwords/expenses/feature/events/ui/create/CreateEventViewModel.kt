package com.inwords.expenses.feature.events.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.UI
import com.inwords.expenses.feature.events.domain.EventsInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class CreateEventViewModel(
    private val navigationController: NavigationController,
    private val eventsInteractor: EventsInteractor,
    private val addParticipantsDestination: Destination,
    private val homeScreenDestination: Destination,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private var confirmJob: Job? = null

    private val _state = MutableStateFlow(CreateEventScreenUiModel(""))
    val state: StateFlow<CreateEventScreenUiModel> = _state

    fun onEventNameChanged(eventName: String) {
        _state.update { value ->
            value.copy(eventName = eventName)
        }
    }

    fun onConfirmClicked() {
        // TODO mvp
        confirmJob?.cancel()
        confirmJob = viewModelScope.launch {
            val state = _state.value
            eventsInteractor.draftEventName(
                eventName = state.eventName
            )
            withContext(UI) {
                navigationController.navigateTo(
                    screen = addParticipantsDestination,
                    popUpTo = homeScreenDestination
                )
            }
        }
    }
}