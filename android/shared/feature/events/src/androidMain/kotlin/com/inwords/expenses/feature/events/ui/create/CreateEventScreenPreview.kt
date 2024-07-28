package com.inwords.expenses.feature.events.ui.create

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
private fun CreateEventScreenPreview() {
    CreateEventScreen(
        state = CreateEventScreenUiModel(
            eventName = "",
        ),
        onEventNameChanged = {},
        onConfirmClicked = {},
    )
}