package com.inwords.expenses.feature.events.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class DeleteEventRequest(
    @SerialName("pinCode")
    val pinCode: String
)
