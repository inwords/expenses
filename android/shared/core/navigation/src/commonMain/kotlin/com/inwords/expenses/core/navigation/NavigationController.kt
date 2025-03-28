package com.inwords.expenses.core.navigation

interface NavigationController {

    fun navigateTo(destination: Destination)

    fun navigateTo(destination: Destination, popUpTo: Destination, launchSingleTop: Boolean = false)

    fun popBackStack()

    fun popBackStack(toDestination: Destination, inclusive: Boolean)
}