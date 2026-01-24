package com.inwords.expenses.feature.menu.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inwords.expenses.core.ui.design.legal.LegalBlock
import com.inwords.expenses.core.ui.design.theme.CommonExTheme
import com.inwords.expenses.core.ui.utils.clipEntryOf
import com.inwords.expenses.core.utils.IO
import expenses.shared.feature.menu.generated.resources.Res
import expenses.shared.feature.menu.generated.resources.menu_add_participants_action
import expenses.shared.feature.menu.generated.resources.menu_choose_person_action
import expenses.shared.feature.menu.generated.resources.menu_copy_action
import expenses.shared.feature.menu.generated.resources.menu_join_other_event
import expenses.shared.feature.menu.generated.resources.menu_open_events_list
import expenses.shared.feature.menu.generated.resources.menu_share_action
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun MenuDialog(
    state: MenuDialogUiModel,
    onJoinEventClicked: () -> Unit,
    onLeaveEventClicked: () -> Unit,
    onChoosePersonClicked: () -> Unit,
    onAddParticipantsClicked: () -> Unit,
    onShareClicked: () -> Unit,
    onPrivacyPolicyClicked: () -> Unit,
    onTermsOfUseClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp),
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onShareClicked)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.CenterVertically),
                imageVector = Icons.Outlined.Share,
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                text = stringResource(Res.string.menu_share_action),
                style = MaterialTheme.typography.bodyLarge
            )

            val coroutineScope = rememberCoroutineScope()
            val clipboard = LocalClipboard.current
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .border(
                        border = AssistChipDefaults.assistChipBorder(true),
                        shape = MaterialTheme.shapes.small
                    )
                    .clickable(
                        enabled = state.shareUrl != null
                    ) {
                        val shareUrl = state.shareUrl ?: return@clickable
                        val eventName = state.eventName
                        coroutineScope.launch(IO) {
                            clipboard.setClipEntry(
                                clipEntryOf(eventName, shareUrl)
                            )
                        }
                    }
                    .padding(8.dp),
                text = stringResource(Res.string.menu_copy_action),
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        HorizontalDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onChoosePersonClicked)
                .padding(16.dp),
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
                text = stringResource(Res.string.menu_choose_person_action),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onAddParticipantsClicked)
                .padding(16.dp),
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.CenterVertically),
                imageVector = Icons.Outlined.Add,
                contentDescription = null
            )
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(Res.string.menu_add_participants_action),
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
                text = stringResource(Res.string.menu_join_other_event),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Row(
            modifier = Modifier
                .clickable(onClick = onLeaveEventClicked)
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.CenterVertically),
                imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                contentDescription = null
            )
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(Res.string.menu_open_events_list),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        HorizontalDivider()

        LegalBlock(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            onPrivacyPolicyClicked = onPrivacyPolicyClicked,
            onTermsOfUseClicked = onTermsOfUseClicked
        )
    }
}

@Preview
@Composable
private fun MenuDialogPreview() {
    CommonExTheme {
        MenuDialog(
            state = MenuDialogUiModel(
                eventName = "Пример события",
                shareUrl = "https://commonex.ru/event/ASDASD?pinCode=1234"
            ),
            onJoinEventClicked = {},
            onLeaveEventClicked = {},
            onChoosePersonClicked = {},
            onAddParticipantsClicked = {},
            onShareClicked = {},
            onPrivacyPolicyClicked = {},
            onTermsOfUseClicked = {},
        )
    }
}

@Preview
@Composable
private fun MenuDialogEmptyShareUrlPreview() {
    CommonExTheme {
        MenuDialog(
            state = MenuDialogUiModel(
                eventName = "Пример события",
                shareUrl = null
            ),
            onJoinEventClicked = {},
            onLeaveEventClicked = {},
            onChoosePersonClicked = {},
            onAddParticipantsClicked = {},
            onShareClicked = {},
            onPrivacyPolicyClicked = {},
            onTermsOfUseClicked = {},
        )
    }
}
