package com.inwords.expenses.feature.events.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AddPersonsToEventRequest(
    @SerialName("users")
    val users: List<CreateUserDto>,

    @SerialName("pinCode")
    val pinCode: String,
)