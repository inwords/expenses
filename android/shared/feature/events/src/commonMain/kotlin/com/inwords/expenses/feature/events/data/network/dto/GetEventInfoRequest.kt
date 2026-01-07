package com.inwords.expenses.feature.events.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GetEventInfoRequest(
    @SerialName("pinCode")
    val pinCode: String? = null,

    @SerialName("token")
    val token: String? = null,
)

