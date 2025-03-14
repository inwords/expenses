package com.inwords.expenses.feature.events.ui.choose_person

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.settings.api.SettingsRepository
import kotlinx.serialization.Serializable

@Serializable
data object ChoosePersonScreenDestination : Destination

fun NavGraphBuilder.addChoosePersonScreen(
    navigationController: NavigationController,
    eventsInteractor: EventsInteractor,
    settingsRepository: SettingsRepository,
    expensesScreenDestination: Destination,
) {
    composable<ChoosePersonScreenDestination> {
        val viewModel = viewModel<ChoosePersonViewModel>(it, factory = viewModelFactory {
            initializer {
                ChoosePersonViewModel(
                    navigationController = navigationController,
                    eventsInteractor = eventsInteractor,
                    settingsRepository = settingsRepository,
                    expensesScreenDestination = expensesScreenDestination,
                )
            }
        })
        ChoosePersonScreen(
            state = viewModel.state.collectAsStateWithLifecycle().value,
            onPersonSelected = viewModel::onPersonSelected,
            onConfirmClicked = viewModel::onConfirmClicked,
        )
    }
}