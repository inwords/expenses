package com.inwords.expenses.feature.events.ui.join

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

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