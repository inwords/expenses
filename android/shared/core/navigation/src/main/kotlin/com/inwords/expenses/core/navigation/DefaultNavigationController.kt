package com.inwords.expenses.core.navigation

import androidx.navigation.NavHostController
import com.inwords.expenses.core.navigation.DefaultNavigationController.NavCommand
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

internal class DefaultNavigationController : NavigationController {

    fun interface NavCommand {

        fun execute(navController: NavHostController)
    }

    private val _navCommands = Channel<NavCommand>(capacity = 10)
    val navCommands: ReceiveChannel<NavCommand> = _navCommands

    override fun navigateTo(screen: Destination) {
        _navCommands.trySend(NavCommand { it.navigate(screen) })
    }

    override fun navigateTo(screen: Destination, popUpTo: Destination) {
        _navCommands.trySend(
            NavCommand {
                it.navigate(screen) {
                    popUpTo(popUpTo)
                }
            }
        )
    }

    override fun popBackStack() {
        _navCommands.trySend(NavCommand { it.popBackStack() })
    }
}