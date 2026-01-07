package com.inwords.expenses.feature.events.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
internal data class CreateEventShareTokenResponse(
    @SerialName("token")
    val token: String,

    @SerialName("expiresAt")
    val expiresAt: Instant,
)
