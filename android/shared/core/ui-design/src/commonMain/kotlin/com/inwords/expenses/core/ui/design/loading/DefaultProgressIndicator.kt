package com.inwords.expenses.core.ui.design.loading

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DefaultProgressIndicator(modifier: Modifier = Modifier) {
    LoadingIndicator(modifier = modifier)
}
