package com.inwords.expenses.feature.events.ui.join

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
object JoinEventScreenDestination : Destination

fun NavGraphBuilder.addJoinEventScreen(
    navigationController: NavigationController,
    eventsInteractor: EventsInteractor,
    expensesScreenDestination: Destination,
) {
    composable<JoinEventScreenDestination> {
        val viewModel = viewModel<JoinEventViewModel>(it, factory = viewModelFactory {
            initializer {
                JoinEventViewModel(
                    navigationController = navigationController,
                    eventsInteractor = eventsInteractor,
                    expensesScreenDestination = expensesScreenDestination
                )
            }
        })
        JoinEventScreen(
            state = viewModel.state.collectAsStateWithLifecycle().value,
            onEventIdChanged = viewModel::onEventIdChanged,
            onEventAccessCodeChanged = viewModel::onEventAccessCodeChanged,
            onConfirmClicked = viewModel::onConfirmClicked,
        )
    }
}