package com.inwords.expenses.feature.events.domain.model

data class Currency(
    val id: Long,
    val serverId: String?,
    val code: String,
    val name: String,
)
