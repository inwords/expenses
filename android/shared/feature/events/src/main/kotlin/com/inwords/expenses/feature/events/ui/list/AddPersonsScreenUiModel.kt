package com.inwords.expenses.feature.events.ui.list

import androidx.compose.runtime.Stable

@Stable
internal data class AddPersonsScreenUiModel(
    val ownerName: String,
    val persons: List<String>,
)
