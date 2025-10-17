package com.inwords.expenses.feature.menu.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.stateInWhileSubscribed
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.ui.choose_person.ChoosePersonScreenDestination
import com.inwords.expenses.feature.events.ui.join.JoinEventScreenDestination
import com.inwords.expenses.feature.share.api.ShareManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class MenuViewModel(
    private val navigationController: NavigationController,
    private val eventsInteractor: EventsInteractor,
    private val shareManagerLazy: Lazy<ShareManager>,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private val emptyState = MenuDialogUiModel("", "")

    private var leaveEventJob: Job? = null

    val state: StateFlow<MenuDialogUiModel> = eventsInteractor.currentEvent
        .map { event ->
            event ?: return@map emptyState

            val serverId = event.event.serverId
            MenuDialogUiModel(
                eventName = event.event.name,
                shareUrl = if (serverId == null) {
                    null
                } else {
                    "https://commonex.ru/event/$serverId?pinCode=${event.event.pinCode}"
                },
            )
        }
        .stateInWhileSubscribed(scope = viewModelScope, initialValue = emptyState)

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
        val state = state.value
        val eventName = state.eventName.ifEmpty { return }
        val shareUrl = state.shareUrl ?: return
        shareManagerLazy.value.shareUrl(eventName, shareUrl)
    }

}