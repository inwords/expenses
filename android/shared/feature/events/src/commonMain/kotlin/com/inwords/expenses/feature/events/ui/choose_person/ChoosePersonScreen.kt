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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.inwords.expenses.core.ui.design.appbar.BasicTopAppBar
import com.inwords.expenses.core.ui.design.button.BasicFloatingActionButton
import com.inwords.expenses.core.ui.design.theme.ExpensesTheme
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.feature.events.ui.choose_person.ChoosePersonScreenUiModel.PersonUiModel
import com.inwords.expenses.feature.events.ui.common.EventInfoBlock
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview

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
            BasicTopAppBar()
        },
        floatingActionButton = {
            BasicFloatingActionButton(
                text = "Продолжить",
                icon = Icons.AutoMirrored.Outlined.ArrowForward,
                onClick = onConfirmClicked,
                enabled = state.persons.any { it.selected },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues)
                .padding(paddingValues)
        ) {
            Column {
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

@Preview
@Composable
private fun ChoosePersonScreenPreview() {
    ExpensesTheme {
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
}
