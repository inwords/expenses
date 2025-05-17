package com.inwords.expenses.feature.menu.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun MenuDialog(
    state: MenuDialogUiModel,
    onJoinEventClicked: () -> Unit,
    onChoosePersonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
        ) {
            Spacer(modifier = Modifier.width(43.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "ID ${state.eventId}, PIN ${state.eventAccessCode}",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        HorizontalDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable(onClick = onChoosePersonClicked),
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.CenterVertically),
                imageVector = Icons.Outlined.Person,
                contentDescription = null
            )
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "Выбрать участника",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Row(
            modifier = Modifier
                .clickable(onClick = onJoinEventClicked)
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.CenterVertically),
                imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                contentDescription = null
            )
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "Войти в другое событие",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
