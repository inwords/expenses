package com.inwords.expenses.feature.events.data.db.converter

import com.inwords.expenses.feature.events.data.db.entity.CurrencyEntity
import com.inwords.expenses.feature.events.data.db.entity.EventEntity
import com.inwords.expenses.feature.events.data.db.entity.PersonEntity
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.events.domain.model.Person

internal fun EventDetails.toEntity() = EventEntity(
    eventId = event.id,
    name = event.name,
    primaryCurrencyId = primaryCurrency.id,
)

internal fun Person.toEntity(): PersonEntity {
    return PersonEntity(
        personId = id,
        name = name,
    )
}

internal fun Currency.toEntity(): CurrencyEntity {
    return CurrencyEntity(
        currencyId = id,
        code = code,
        name = name,
    )
}