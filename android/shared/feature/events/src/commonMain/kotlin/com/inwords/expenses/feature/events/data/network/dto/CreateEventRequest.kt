package com.inwords.expenses.feature.events.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateEventRequest(
    @SerialName("name")
    val name: String,

    @SerialName("currencyId")
    val currencyId: String,

    @SerialName("users")
    val users: List<CreateUserDto>,

    @SerialName("pinCode")
    val pinCode: String
)