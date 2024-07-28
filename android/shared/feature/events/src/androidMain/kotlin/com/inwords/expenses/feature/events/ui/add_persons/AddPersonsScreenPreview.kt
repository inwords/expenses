package com.inwords.expenses.feature.events.ui.add_persons

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
private fun AddPersonsScreenPreview() {
    AddPersonsScreen(
        state = AddPersonsScreenUiModel(
            ownerName = "",
            persons = listOf("Анжела", "Саша")
        ),
        onOwnerNameChanged = {},
        onParticipantNameChanged = { _, _ -> },
        onAddParticipantClicked = {},
        onConfirmClicked = {},
    )
}