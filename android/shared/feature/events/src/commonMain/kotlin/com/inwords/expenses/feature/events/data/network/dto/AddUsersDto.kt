package com.inwords.expenses.feature.events.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AddUsersDto(
    @SerialName("users")
    val users: List<CreateUserDto>,
)