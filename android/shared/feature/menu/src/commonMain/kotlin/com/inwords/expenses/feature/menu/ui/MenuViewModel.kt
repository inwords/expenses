package com.inwords.expenses.feature.menu.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.collectIn
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.ui.choose_person.ChoosePersonScreenDestination
import com.inwords.expenses.feature.events.ui.join.JoinEventScreenDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class MenuViewModel(
    private val navigationController: NavigationController,
    eventsInteractor: EventsInteractor,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private val emptyState = MenuDialogUiModel("", "")

    private val _state = MutableStateFlow(emptyState)
    val state: StateFlow<MenuDialogUiModel> = _state

    init {
        eventsInteractor.currentEvent.collectIn(viewModelScope) { event ->
            if (event == null) {
                _state.value = emptyState
                return@collectIn
            }

            _state.value = MenuDialogUiModel(
                eventId = event.event.id.toString(),
                eventAccessCode = event.event.pinCode,
            )
        }
    }

    fun onJoinEventClicked() {
        navigationController.navigateTo(JoinEventScreenDestination)
    }

    fun onChoosePersonClicked() {
        navigationController.navigateTo(ChoosePersonScreenDestination)
    }
}