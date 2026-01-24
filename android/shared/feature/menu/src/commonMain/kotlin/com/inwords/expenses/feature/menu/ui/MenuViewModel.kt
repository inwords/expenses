package com.inwords.expenses.feature.menu.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.UI
import com.inwords.expenses.core.utils.stateInWhileSubscribed
import com.inwords.expenses.feature.events.domain.GetCurrentEventStateUseCase
import com.inwords.expenses.feature.events.domain.LeaveEventUseCase
import com.inwords.expenses.feature.events.ui.add_participants.AddParticipantsToEventPaneDestination
import com.inwords.expenses.feature.events.ui.choose_person.ChoosePersonPaneDestination
import com.inwords.expenses.feature.events.ui.join.JoinEventPaneDestination
import com.inwords.expenses.feature.share.api.ShareManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class MenuViewModel(
    private val navigationController: NavigationController,
    getCurrentEventStateUseCase: GetCurrentEventStateUseCase,
    private val leaveEventUseCase: LeaveEventUseCase,
    private val shareManagerLazy: Lazy<ShareManager>,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private val emptyState = MenuDialogUiModel("", "")

    private var leaveEventJob: Job? = null

    val state: StateFlow<MenuDialogUiModel> = getCurrentEventStateUseCase.currentEvent
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
        navigationController.popBackStack()
        navigationController.navigateTo(destination = JoinEventPaneDestination())
    }

    fun onLeaveEventClicked() {
        if (leaveEventJob != null) return
        leaveEventJob = viewModelScope.launch {
            leaveEventUseCase.leaveEvent()
            navigationController.popBackStack()
            leaveEventJob = null
        }
    }

    fun onChoosePersonClicked() {
        navigationController.popBackStack()
        navigationController.navigateTo(ChoosePersonPaneDestination)
    }

    fun onAddParticipantClicked() {
        navigationController.popBackStack()
        navigationController.navigateTo(AddParticipantsToEventPaneDestination)
    }

    fun onShareClicked() {
        val state = state.value
        val eventName = state.eventName.ifEmpty { return }
        val shareUrl = state.shareUrl ?: return
        viewModelScope.launch(UI) {
            shareManagerLazy.value.shareUrl(eventName, shareUrl)
        }
    }

    fun onPrivacyPolicyClicked() {
        // URL opening is handled in the composable using openUriSafe
        navigationController.popBackStack()
    }

    fun onTermsOfUseClicked() {
        // URL opening is handled in the composable using openUriSafe
        navigationController.popBackStack()
    }

}