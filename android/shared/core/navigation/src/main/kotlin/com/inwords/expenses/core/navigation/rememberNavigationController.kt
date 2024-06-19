package com.inwords.expenses.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController

@Composable
fun rememberNavigationController(navController: NavHostController): NavigationController {
    val navigationController = viewModel<NavigationViewModel>(factory = viewModelFactory {
        initializer { NavigationViewModel() }
    }).navigationController
    LaunchedEffect(key1 = navController) {
        for (command in navigationController.navCommands) {
            command.execute(navController)
        }
    }
    return navigationController
}

private class NavigationViewModel : ViewModel() {

    val navigationController = DefaultNavigationController()
}