package com.inwords.expenses.feature.events.domain.model

data class Event(
    val id: Long,
    val serverId: Long,
    val name: String,
    val pinCode: String,
)