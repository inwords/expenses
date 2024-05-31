package com.inwords.expenses.feature.events.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inwords.expenses.feature.events.ui.common.PersonNameField

@Composable
internal fun AddPersonsScreen(
    modifier: Modifier = Modifier,
    state: AddPersonsScreenUiModel,
    onOwnerNameChanged: (String) -> Unit,
    onParticipantNameChanged: (Int, String) -> Unit,
    onAddParticipantClicked: () -> Unit,
    onConfirmClicked: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = modifier
                .verticalScroll(scrollState)
                .align(Alignment.Center),
        ) {
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = "Меня зовут",
                style = MaterialTheme.typography.headlineMedium
            )
            PersonNameField(
                modifier = Modifier.padding(bottom = 16.dp),
                participantName = state.ownerName,
                onParticipantNameChanged = onOwnerNameChanged
            )

            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = "Ещё участвуют",
                style = MaterialTheme.typography.headlineMedium
            )
            state.persons.forEachIndexed { i, name ->
                PersonNameField(
                    modifier = Modifier.padding(bottom = 8.dp),
                    participantName = name,
                    onParticipantNameChanged = { onParticipantNameChanged(i, it) }
                )
            }
            OutlinedButton(onClick = onAddParticipantClicked) {
                Text(text = "Добавить участника")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Outlined.Add, contentDescription = null)
            }
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