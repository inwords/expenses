package com.inwords.expenses.feature.events.ui.join

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inwords.expenses.feature.events.ui.common.EventAccessCodeField
import com.inwords.expenses.feature.events.ui.common.EventIdField

@Composable
internal fun JoinEventScreen(
    modifier: Modifier = Modifier,
    state: JoinEventScreenUiModel,
    onEventIdChanged: (String) -> Unit,
    onEventAccessCodeChanged: (String) -> Unit,
    onConfirmClicked: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier.align(Alignment.Center),
        ) {
            EventIdField(
                modifier = Modifier.padding(bottom = 16.dp),
                eventId = state.eventId,
                onEventIdChanged = onEventIdChanged
            )

            EventAccessCodeField(
                eventAccessCode = state.eventAccessCode,
                onDone = onConfirmClicked,
                onEventAccessCodeChanged = onEventAccessCodeChanged,
            )
        }

        FilledTonalButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp),
            onClick = onConfirmClicked
        ) {
            Text(text = "К событию")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun EventsScreenPreview() {
    JoinEventScreen(
        state = JoinEventScreenUiModel(
            eventId = "",
            eventAccessCode = ""
        ),
        onEventIdChanged = {},
        onEventAccessCodeChanged = {},
        onConfirmClicked = {},
    )
}