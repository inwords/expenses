package com.inwords.expenses.feature.events.ui.choose_person

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.feature.events.ui.choose_person.ChoosePersonScreenUiModel.PersonUiModel
import com.inwords.expenses.feature.events.ui.common.EventInfoBlock

@Composable
internal fun ChoosePersonScreen(
    modifier: Modifier = Modifier,
    state: SimpleScreenState<ChoosePersonScreenUiModel>,
    onPersonSelected: (Long) -> Unit,
    onConfirmClicked: () -> Unit,
) {

    when (state) {
        is SimpleScreenState.Loading -> {
            LoadingState(modifier = modifier)
        }

        is SimpleScreenState.Empty -> {
            Box(modifier = modifier.fillMaxSize()) {
                Text(
                    text = "No persons available",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        is SimpleScreenState.Error -> {
            Box(modifier = modifier.fillMaxSize()) {
                Text(
                    text = "An error occurred",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        is SimpleScreenState.Success -> {
            ChoosePersonContent(
                modifier = modifier,
                state = state.data,
                onPersonSelected = onPersonSelected,
                onConfirmClicked = onConfirmClicked
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChoosePersonContent(
    modifier: Modifier = Modifier,
    state: ChoosePersonScreenUiModel,
    onPersonSelected: (Long) -> Unit,
    onConfirmClicked: () -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp),
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "CommonEx",
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (state.persons.any { it.selected }) {
                        onConfirmClicked()
                    }
                },
                containerColor = if (state.persons.any { it.selected }) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                },
                contentColor = if (state.persons.any { it.selected }) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f)
                }
            ) {
                Text(text = "Продолжить")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
            }
        }
    ) { paddingValues ->
        val topAndHorizontalPaddings = PaddingValues(
            top = paddingValues.calculateTopPadding(),
            start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
            end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(topAndHorizontalPaddings)
                .padding(topAndHorizontalPaddings)
        ) {
            EventInfoBlock(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                eventName = state.eventName,
                currentPersonName = state.selectedPersonName
            )

            Text(
                text = "Ваше имя",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 4.dp)
            )

            val bottomPadding = paddingValues.calculateBottomPadding()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .consumeWindowInsets(PaddingValues(bottom = bottomPadding))
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 88.dp + bottomPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    count = state.persons.size,
                    key = { index -> state.persons[index].id }
                ) { index ->
                    val person = state.persons[index]
                    PersonSelectionItem(
                        person = person,
                        onPersonSelected = onPersonSelected,
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun PersonSelectionItem(
    person: PersonUiModel,
    onPersonSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val animatedBorderColor by animateColorAsState(
        targetValue = if (person.selected) colorScheme.primary else colorScheme.outline,
        animationSpec = tween()
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onPersonSelected(person.id) }
            .border(
                width = 1.dp,
                color = animatedBorderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val animatedBackgroundColor by animateColorAsState(
            targetValue = if (person.selected) colorScheme.primary else colorScheme.surfaceVariant,
            animationSpec = tween()
        )
        val animatedTextColor by animateColorAsState(
            targetValue = if (person.selected) colorScheme.onPrimary else colorScheme.onSurfaceVariant,
            animationSpec = tween()
        )

        Text(
            modifier = Modifier
                .background(
                    color = animatedBackgroundColor,
                    shape = CircleShape
                )
                .size(32.dp)
                .wrapContentHeight(Alignment.CenterVertically),
            text = person.name.firstOrNull()?.toString() ?: "",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = animatedTextColor
        )

        Spacer(modifier = Modifier.width(12.dp))

        val animatedFontWeight by animateIntAsState(
            targetValue = if (person.selected) FontWeight.SemiBold.weight else FontWeight.Normal.weight,
            animationSpec = tween()
        )
        val animatedNameTextColor by animateColorAsState(
            targetValue = if (person.selected) colorScheme.onPrimaryContainer else colorScheme.onSurface,
            animationSpec = tween()
        )

        Text(
            text = person.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight(animatedFontWeight),
            color = animatedNameTextColor
        )
    }
}