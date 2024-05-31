package com.inwords.expenses.feature.events.ui.join

import androidx.compose.runtime.Stable

@Stable
internal data class JoinEventScreenUiModel(
    val eventId: String,
    val eventAccessCode: String,
)