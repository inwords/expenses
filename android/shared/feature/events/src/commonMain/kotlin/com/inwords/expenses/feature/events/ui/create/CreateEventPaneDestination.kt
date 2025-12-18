package com.inwords.expenses.feature.events.ui.create

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavModule
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.domain.GetCurrenciesUseCase
import kotlinx.serialization.Serializable

@Serializable
object CreateEventPaneDestination : Destination

fun getCreateEventPaneNavModule(
    navigationController: NavigationController,
    eventsInteractorLazy: Lazy<EventsInteractor>,
    getCurrenciesUseCaseLazy: Lazy<GetCurrenciesUseCase>,
    expensesScreenDestination: Destination,
): NavModule {
    return NavModule(CreateEventPaneDestination.serializer()) {
        entry<CreateEventPaneDestination> {
            val viewModel = viewModel<CreateEventViewModel>(factory = viewModelFactory {
                initializer {
                    CreateEventViewModel(
                        navigationController = navigationController,
                        eventsInteractor = eventsInteractorLazy.value,
                        getCurrenciesUseCase = getCurrenciesUseCaseLazy.value,
                    )
                }
            })
            CreateEventPane(
                state = viewModel.state.collectAsStateWithLifecycle().value,
                onEventNameChanged = viewModel::onEventNameChanged,
                onCurrencyClicked = viewModel::onCurrencyClicked,
                onConfirmClicked = viewModel::onConfirmClicked,
                onNavIconClicked = viewModel::onNavIconClicked,
            )
        }
    }
}
