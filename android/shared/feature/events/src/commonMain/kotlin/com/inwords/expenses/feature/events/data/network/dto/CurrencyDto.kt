package com.inwords.expenses.feature.events.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CurrencyDto(
    @SerialName("id")
    val id: String,

    @SerialName("code")
    val code: String,
)