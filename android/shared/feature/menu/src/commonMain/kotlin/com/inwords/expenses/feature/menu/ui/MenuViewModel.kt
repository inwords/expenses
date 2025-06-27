package com.inwords.expenses.feature.menu.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.collectIn
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.ui.choose_person.ChoosePersonScreenDestination
import com.inwords.expenses.feature.events.ui.join.JoinEventScreenDestination
import com.inwords.expenses.feature.share.api.ShareManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class MenuViewModel(
    private val navigationController: NavigationController,
    private val eventsInteractor: EventsInteractor,
    private val shareManagerLazy: Lazy<ShareManager>,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private val emptyState = MenuDialogUiModel("", "", "")

    private var leaveEventJob: Job? = null

    private val _state = MutableStateFlow(emptyState)
    val state: StateFlow<MenuDialogUiModel> = _state

    init {
        eventsInteractor.currentEvent.collectIn(viewModelScope) { event ->
            if (event == null) {
                _state.value = emptyState
                return@collectIn
            }

            _state.value = MenuDialogUiModel(
                eventName = event.event.name,
                eventId = event.event.serverId.orEmpty(),
                eventAccessCode = event.event.pinCode,
            )
        }
    }

    fun onJoinEventClicked() {
        navigationController.navigateTo(JoinEventScreenDestination())
    }

    fun onLeaveEventClicked() {
        if (leaveEventJob != null) return
        leaveEventJob = viewModelScope.launch {
            eventsInteractor.leaveEvent()
            navigationController.popBackStack()
            leaveEventJob = null
        }
    }

    fun onChoosePersonClicked() {
        navigationController.navigateTo(ChoosePersonScreenDestination)
    }

    fun onShareClicked() {
        val state = _state.value
        val eventName = state.eventName.ifEmpty { return }
        val eventId = state.eventId.ifEmpty { return }
        val eventAccessCode = state.eventAccessCode.ifEmpty { return }
        shareManagerLazy.value.shareUrl(eventName, "https://commonex.ru/event/$eventId?pinCode=$eventAccessCode")
    }

}