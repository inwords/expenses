package com.inwords.expenses.feature.expenses.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GetEventExpensesRequest(
    @SerialName("pinCode")
    val pinCode: String,
)
