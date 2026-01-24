package com.inwords.expenses.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ErrorResponseDto(
    @SerialName("statusCode")
    val statusCode: Int?,

    @SerialName("code")
    val code: String?,

    @SerialName("message")
    val message: String?,
)
