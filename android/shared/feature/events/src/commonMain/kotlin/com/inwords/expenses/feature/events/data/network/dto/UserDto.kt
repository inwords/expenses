package com.inwords.expenses.feature.events.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserDto(
    @SerialName("name")
    val name: String,

    @SerialName("eventId")
    val eventId: Long,

    @SerialName("id")
    val id: String
)