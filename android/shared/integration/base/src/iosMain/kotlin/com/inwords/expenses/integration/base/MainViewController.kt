package com.inwords.expenses.integration.base

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.inwords.expenses.core.ui.design.theme.ExpensesTheme
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController {
    ExpensesTheme {
        MainNavHost(modifier = Modifier.fillMaxSize())
    }
}