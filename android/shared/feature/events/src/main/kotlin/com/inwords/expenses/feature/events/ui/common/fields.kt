package com.inwords.expenses.feature.events.ui.common

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType

@Composable
internal fun EventNameField(
    eventName: String,
    onEventNameChanged: (String) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        modifier = modifier,
        value = eventName,
        onValueChange = onEventNameChanged,
        placeholder = { Text("Название события") },
        singleLine = true,
        textStyle = MaterialTheme.typography.headlineMedium,
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
internal fun EventIdField(
    eventId: String,
    onEventIdChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        modifier = modifier,
        value = eventId,
        onValueChange = onEventIdChanged,
        placeholder = { Text("ID события") },
        singleLine = true,
        textStyle = MaterialTheme.typography.headlineMedium,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        )
    )
}

@Composable
internal fun EventAccessCodeField(
    eventAccessCode: String,
    onEventAccessCodeChanged: (String) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier,
        value = eventAccessCode,
        onValueChange = onEventAccessCodeChanged,
        placeholder = { Text("Код доступа") },
        singleLine = true,
        textStyle = MaterialTheme.typography.headlineMedium,
        keyboardActions = KeyboardActions(onDone = { onDone.invoke() }),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
internal fun PersonNameField(
    participantName: String,
    imeAction: ImeAction,
    onParticipantNameChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    onImeAction: () -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier,
        value = participantName,
        onValueChange = onParticipantNameChanged,
        placeholder = { Text("Имя") },
        singleLine = true,
        textStyle = MaterialTheme.typography.headlineMedium,
        keyboardActions = KeyboardActions(onAny = { onImeAction.invoke() }),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            keyboardType = KeyboardType.Text,
            imeAction = imeAction
        )
    )
}
