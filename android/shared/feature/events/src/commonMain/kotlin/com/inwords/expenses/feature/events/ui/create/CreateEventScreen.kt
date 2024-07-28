package com.inwords.expenses.feature.events.ui.create

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inwords.expenses.feature.events.ui.common.EventNameField

@Composable
internal fun CreateEventScreen(
    modifier: Modifier = Modifier,
    state: CreateEventScreenUiModel,
    onEventNameChanged: (String) -> Unit,
    onConfirmClicked: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        EventNameField(
            modifier = modifier.align(Alignment.Center),
            eventName = state.eventName,
            onDone = onConfirmClicked,
            onEventNameChanged = onEventNameChanged
        )

        FilledTonalButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp),
            onClick = onConfirmClicked
        ) {
            Text(text = "Участники")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
        }
    }
}
