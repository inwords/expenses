package com.inwords.expenses.feature.menu.ui

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.scene.DialogSceneStrategy.Companion.dialog
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavModule
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.share.api.ShareManager
import kotlinx.serialization.Serializable

@Serializable
object MenuDialogDestination : Destination

fun getMenuDialogNavModule(
    navigationController: NavigationController,
    eventsInteractor: EventsInteractor,
    shareManagerLazy: Lazy<ShareManager>
): NavModule {
    return NavModule(MenuDialogDestination.serializer()) {
        entry<MenuDialogDestination>(metadata = dialog()) {
            val viewModel = viewModel<MenuViewModel>(factory = viewModelFactory {
                initializer {
                    MenuViewModel(
                        navigationController = navigationController,
                        eventsInteractor = eventsInteractor,
                        shareManagerLazy = shareManagerLazy
                    )
                }
            })
            MenuDialog(
                state = viewModel.state.collectAsStateWithLifecycle().value,
                onJoinEventClicked = viewModel::onJoinEventClicked,
                onLeaveEventClicked = viewModel::onLeaveEventClicked,
                onChoosePersonClicked = viewModel::onChoosePersonClicked,
                onShareClicked = viewModel::onShareClicked,
                onPrivacyPolicyClicked = viewModel::onPrivacyPolicyClicked,
                onTermsOfUseClicked = viewModel::onTermsOfUseClicked,
            )
        }
    }
}
