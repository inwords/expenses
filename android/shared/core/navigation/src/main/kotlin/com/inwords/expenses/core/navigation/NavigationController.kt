package com.inwords.expenses.core.navigation

import androidx.annotation.MainThread
import androidx.compose.runtime.Stable

@Stable
interface NavigationController {
    @MainThread
    fun navigateTo(screen: Destination)

    @MainThread
    fun popBackStack()
}