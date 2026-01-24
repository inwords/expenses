package com.inwords.expenses.feature.menu.ui

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.scene.DialogSceneStrategy.Companion.dialog
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavModule
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.feature.events.domain.CreateShareTokenUseCase
import com.inwords.expenses.feature.events.domain.GetCurrentEventStateUseCase
import com.inwords.expenses.feature.events.domain.LeaveEventUseCase
import com.inwords.expenses.feature.share.api.ShareManager
import kotlinx.serialization.Serializable

@Serializable
object MenuDialogDestination : Destination

fun getMenuDialogNavModule(
    navigationController: NavigationController,
    getCurrentEventStateUseCaseLazy: Lazy<GetCurrentEventStateUseCase>,
    leaveEventUseCaseLazy: Lazy<LeaveEventUseCase>,
    shareManagerLazy: Lazy<ShareManager>,
    createShareTokenUseCaseLazy: Lazy<CreateShareTokenUseCase>,
): NavModule {
    return NavModule(MenuDialogDestination.serializer()) {
        entry<MenuDialogDestination>(metadata = dialog()) {
            val viewModel = viewModel<MenuViewModel>(factory = viewModelFactory {
                initializer {
                    MenuViewModel(
                        navigationController = navigationController,
                        getCurrentEventStateUseCase = getCurrentEventStateUseCaseLazy.value,
                        leaveEventUseCase = leaveEventUseCaseLazy.value,
                        shareManagerLazy = shareManagerLazy,
                        createShareTokenUseCaseLazy = createShareTokenUseCaseLazy,
                    )
                }
            })
            MenuDialog(
                state = viewModel.state.collectAsStateWithLifecycle().value,
                onJoinEventClicked = viewModel::onJoinEventClicked,
                onLeaveEventClicked = viewModel::onLeaveEventClicked,
                onChoosePersonClicked = viewModel::onChoosePersonClicked,
                onAddParticipantsClicked = viewModel::onAddParticipantClicked,
                onShareClicked = viewModel::onShareClicked,
                onCopyClicked = viewModel::onCopyClicked,
                onTextCopied = viewModel::onTextCopied,
                onPrivacyPolicyClicked = viewModel::onPrivacyPolicyClicked,
                onTermsOfUseClicked = viewModel::onTermsOfUseClicked,
            )
        }
    }
}
