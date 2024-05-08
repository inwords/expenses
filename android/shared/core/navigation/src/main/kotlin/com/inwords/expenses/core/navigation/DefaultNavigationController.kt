package com.inwords.expenses.core.navigation

import androidx.navigation.NavHostController

class DefaultNavigationController(
    private val navController: NavHostController
) : NavigationController {

    override fun navigateTo(screen: Destination) {
        navController.navigate(screen)
    }

    override fun popBackStack() {
        navController.popBackStack()
    }
}