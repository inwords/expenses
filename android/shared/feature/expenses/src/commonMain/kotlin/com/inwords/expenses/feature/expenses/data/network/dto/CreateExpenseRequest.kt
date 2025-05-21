package com.inwords.expenses.feature.expenses.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateExpenseRequest(

    @SerialName("description")
    val description: String,

    @SerialName("userWhoPaidId")
    val userWhoPaidId: Long,

    @SerialName("currencyId")
    val currencyId: Long,

    @SerialName("expenseType")
    val expenseType: String,

    @SerialName("splitInformation")
    val splitInformation: List<SplitInformationRequest>
)