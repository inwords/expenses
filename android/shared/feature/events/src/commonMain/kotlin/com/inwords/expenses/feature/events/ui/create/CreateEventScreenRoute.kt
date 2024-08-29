package com.inwords.expenses.feature.events.ui.create

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
object CreateEventScreenDestination : Destination

fun NavGraphBuilder.addCreateEventScreen(
    navigationController: NavigationController,
    eventsInteractor: EventsInteractor,
    expensesScreenDestination: Destination,
) {
    composable<CreateEventScreenDestination> {
        val viewModel = viewModel<CreateEventViewModel>(it, factory = viewModelFactory {
            initializer {
                CreateEventViewModel(
                    navigationController = navigationController,
                    eventsInteractor = eventsInteractor,
                    expensesScreenDestination = expensesScreenDestination,
                )
            }
        })
        CreateEventScreen(
            state = viewModel.state.collectAsStateWithLifecycle().value,
            onEventNameChanged = viewModel::onEventNameChanged,
            onCurrencyClicked = viewModel::onCurrencyClicked,
            onConfirmClicked = viewModel::onConfirmClicked,
        )
    }
}