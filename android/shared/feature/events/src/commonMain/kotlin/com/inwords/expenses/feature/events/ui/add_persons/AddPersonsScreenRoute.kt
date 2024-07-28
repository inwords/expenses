package com.inwords.expenses.feature.events.ui.add_persons

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.feature.events.domain.EventsInteractor
import kotlinx.serialization.Serializable

@Serializable
object AddPersonsScreenDestination : Destination

fun NavGraphBuilder.addAddPersonsScreen(
    navigationController: NavigationController,
    eventsInteractor: EventsInteractor,
    expensesScreenDestination: Destination,
) {
    composable<AddPersonsScreenDestination> {
        val viewModel = viewModel<AddPersonsViewModel>(it, factory = viewModelFactory {
            initializer {
                AddPersonsViewModel(
                    navigationController = navigationController,
                    eventsInteractor = eventsInteractor,
                    expensesScreenDestination = expensesScreenDestination
                )
            }
        })
        AddPersonsScreen(
            state = viewModel.state.collectAsStateWithLifecycle().value,
            onOwnerNameChanged = viewModel::onOwnerNameChanged,
            onParticipantNameChanged = viewModel::onParticipantNameChanged,
            onAddParticipantClicked = viewModel::onAddParticipantClicked,
            onConfirmClicked = viewModel::onConfirmClicked,
        )
    }
}