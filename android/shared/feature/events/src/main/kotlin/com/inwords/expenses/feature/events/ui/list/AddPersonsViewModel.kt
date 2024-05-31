package com.inwords.expenses.feature.events.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.DefaultNavigationController
import com.inwords.expenses.core.navigation.Destination
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

internal class AddPersonsViewModel(
    private val navigationController: DefaultNavigationController,
    private val eventsInteractor: EventsInteractor,
    private val expensesScreenDestination: Destination,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private var confirmJob: Job? = null

    private val _state = MutableStateFlow(AddPersonsScreenUiModel("", emptyList()))
    val state: StateFlow<AddPersonsScreenUiModel> = _state

    fun onOwnerNameChanged(ownerName: String) {
        _state.update { value ->
            value.copy(ownerName = ownerName)
        }
    }

    fun onParticipantNameChanged(index: Int, participantName: String) {
        _state.update { value ->
            value.copy(persons = value.persons.toMutableList().apply {
                set(index, participantName)
            })
        }
    }

    fun onAddParticipantClicked() {
        _state.update { value ->
            value.copy(persons = value.persons + "")
        }
    }

    fun onConfirmClicked() {
        // TODO mvp
        confirmJob?.cancel()
        confirmJob = viewModelScope.launch {
            val state = _state.value

            eventsInteractor.draftOwner(state.ownerName)
            eventsInteractor.draftPersons(state.persons)
            eventsInteractor.createEvent()

            withContext(UI) {
                navigationController.navigateTo(expensesScreenDestination)
            }
        }
    }
}