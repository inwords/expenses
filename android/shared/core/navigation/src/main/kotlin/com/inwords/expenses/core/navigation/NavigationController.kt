package com.inwords.expenses.core.navigation

import androidx.compose.runtime.Stable

@Stable
interface NavigationController {
    fun navigateTo(screen: Destination)
    fun popBackStack()
}