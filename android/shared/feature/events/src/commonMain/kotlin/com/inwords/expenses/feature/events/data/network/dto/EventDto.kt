package com.inwords.expenses.feature.events.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class EventDto(
    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String,

    @SerialName("currencyId")
    val currencyId: String,

    @SerialName("pinCode")
    val pinCode: String,

    @SerialName("users")
    val users: List<UserDto>,
)