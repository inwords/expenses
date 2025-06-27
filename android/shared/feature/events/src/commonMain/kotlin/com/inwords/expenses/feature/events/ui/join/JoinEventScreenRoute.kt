package com.inwords.expenses.feature.events.ui.join

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.navigation.deeplinkHostPath
import com.inwords.expenses.feature.events.domain.EventsInteractor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JoinEventScreenDestination(
    @SerialName("eventId")
    val eventId: String, // no default value so that is is added as a **path parameter** to deep link
    @SerialName("pinCode")
    val pinCode: String = "",
) : Destination {

    constructor() : this(eventId = "")
}

fun NavGraphBuilder.addJoinEventScreen(
    navigationController: NavigationController,
    eventsInteractor: EventsInteractor,
) {
    composable<JoinEventScreenDestination>(
        deepLinks = listOf(navDeepLink(basePath = "$deeplinkHostPath/event", route = JoinEventScreenDestination::class) {})
    ) { backStackEntry ->
        val destination = backStackEntry.toRoute<JoinEventScreenDestination>()

        val viewModel = viewModel<JoinEventViewModel>(backStackEntry, factory = viewModelFactory {
            initializer {
                JoinEventViewModel(
                    navigationController = navigationController,
                    eventsInteractor = eventsInteractor,
                    initialEventId = destination.eventId,
                    initialPinCode = destination.pinCode,
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