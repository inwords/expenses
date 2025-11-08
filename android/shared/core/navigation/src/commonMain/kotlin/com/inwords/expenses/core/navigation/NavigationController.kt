package com.inwords.expenses.core.navigation

import androidx.navigation3.runtime.NavBackStack

interface NavigationController {

    fun navigateTo(destination: Destination)

    fun navigateTo(destination: Destination, popUpTo: Destination, launchSingleTop: Boolean = false)

    fun popBackStack()

    fun popBackStack(toDestination: Destination, inclusive: Boolean)
}

interface AttachableNavigationController : NavigationController {

    fun attachTo(navBackStack: NavBackStack<Destination>)
}
