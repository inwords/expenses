package com.inwords.expenses.feature.events.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun EventInfoBlock(
    eventName: String,
    currentPersonName: String?,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier
            .padding(horizontal = 16.dp),
        text = if (currentPersonName == null) {
            eventName
        } else {
            "$eventName — $currentPersonName"
        },
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center
    )
}