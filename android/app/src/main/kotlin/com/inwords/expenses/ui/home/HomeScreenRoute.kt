package com.inwords.expenses.ui.home

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.inwords.expenses.core.navigation.Destination
import kotlinx.serialization.Serializable

@Serializable
object HomeScreenDestination : Destination

fun NavGraphBuilder.homeScreen(
    onNavigateToExpenses: () -> Unit,
    onNavigateToCreateEvent: () -> Unit,
    onNavigateToJoinEvent: () -> Unit,
) {
    composable<HomeScreenDestination> {
        val viewModel = viewModel<HomeViewModel>(it)
        HomeScreen(
            onNavigateToExpenses = onNavigateToExpenses,
            onNavigateToCreateEvent = onNavigateToCreateEvent,
            onNavigateToJoinEvent = onNavigateToJoinEvent,
            state = viewModel.state.collectAsStateWithLifecycle().value,
        )
    }
}