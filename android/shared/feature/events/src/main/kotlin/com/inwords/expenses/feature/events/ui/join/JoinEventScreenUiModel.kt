package com.inwords.expenses.feature.events.ui.join

import androidx.compose.runtime.Immutable

@Immutable
internal data class JoinEventScreenUiModel(
    val eventId: String,
    val eventAccessCode: String,
)