package com.inwords.expenses.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import com.inwords.expenses.core.utils.DF
import kotlinx.coroutines.withContext

@Composable
fun HandleDeeplinks(
    deeplinkProvider: DeeplinkProvider,
    navigationController: NavigationController,
    navDeepLinks: Set<NavDeepLink<out Destination>>,
) {
    val controllerRef = rememberUpdatedState(navigationController)
    val navDeepLinksRef = rememberUpdatedState(navDeepLinks)

    LaunchedEffect(deeplinkProvider) {
        withContext(DF) {
            deeplinkProvider.latestDeeplink()
                .collect { deeplink ->
                    val destination = navDeepLinksRef.value.firstNotNullOfOrNull { it.getDestinationIfMatches(deeplink) }
                    if (destination != null) {
                        controllerRef.value.navigateTo(destination)
                    }
                    // FIXME: add logging
                }
        }
    }
}
