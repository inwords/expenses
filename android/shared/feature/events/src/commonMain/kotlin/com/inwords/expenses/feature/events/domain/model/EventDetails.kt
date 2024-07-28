package com.inwords.expenses.feature.events.domain.model

data class EventDetails(
    val event: Event,
    val currencies: List<Currency>,
    val persons: List<Person>,
    val primaryCurrency: Currency,
)
