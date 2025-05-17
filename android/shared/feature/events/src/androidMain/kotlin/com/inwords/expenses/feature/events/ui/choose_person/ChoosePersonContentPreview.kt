package com.inwords.expenses.feature.events.ui.choose_person

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.feature.events.ui.choose_person.ChoosePersonScreenUiModel.PersonUiModel
import kotlinx.collections.immutable.persistentListOf

@Preview(showBackground = true)
@Composable
private fun ChoosePersonScreenPreview() {
    ChoosePersonScreen(
        state = SimpleScreenState.Success(
            ChoosePersonScreenUiModel(
                eventId = 1,
                eventName = "Weekend Trip",
                selectedPersonName = "John Doe",
                persons = persistentListOf(
                    PersonUiModel(id = 1, name = "John Doe", selected = true),
                    PersonUiModel(id = 2, name = "Jane Smith", selected = false),
                    PersonUiModel(id = 3, name = "Peter Jones", selected = false),
                )
            )
        ),
        onPersonSelected = {},
        onConfirmClicked = {}
    )
}
