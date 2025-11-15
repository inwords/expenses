package com.inwords.expenses.feature.events.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import expenses.shared.feature.events.generated.resources.Res
import expenses.shared.feature.events.generated.resources.events_info_with_person
import org.jetbrains.compose.resources.stringResource

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
            stringResource(Res.string.events_info_with_person, eventName, currentPersonName)
        },
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center
    )
}