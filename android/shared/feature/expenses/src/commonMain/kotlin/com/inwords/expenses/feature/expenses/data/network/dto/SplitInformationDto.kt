package com.inwords.expenses.feature.expenses.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SplitInformationDto(
    @SerialName("amount")
    val amount: String,

    @SerialName("userId")
    val userId: Long
)