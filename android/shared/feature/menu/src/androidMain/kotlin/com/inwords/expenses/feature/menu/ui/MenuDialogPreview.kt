package com.inwords.expenses.feature.menu.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = false)
@Composable
private fun MenuDialogPreview() {
    MenuDialog(
        state = MenuDialogUiModel(
            eventId = "1234",
            eventAccessCode = "1111"
        ),
        onJoinEventClicked = {},
    )
}
