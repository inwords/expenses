package com.inwords.expenses.feature.events.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class EventShareToken(
    val token: String,
    val expiresAt: Instant,
)
