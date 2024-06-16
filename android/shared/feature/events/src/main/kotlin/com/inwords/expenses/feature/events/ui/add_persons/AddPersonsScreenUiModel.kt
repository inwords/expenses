package com.inwords.expenses.feature.events.ui.add_persons

import androidx.compose.runtime.Immutable

@Immutable
internal data class AddPersonsScreenUiModel(
    val ownerName: String,
    val persons: List<String>,
)
