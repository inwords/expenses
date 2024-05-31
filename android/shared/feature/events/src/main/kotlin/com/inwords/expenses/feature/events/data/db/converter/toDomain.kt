package com.inwords.expenses.feature.events.data.db.converter

import com.inwords.expenses.feature.events.data.db.entity.CurrencyEntity
import com.inwords.expenses.feature.events.data.db.entity.EventEntity
import com.inwords.expenses.feature.events.data.db.entity.EventWithDetailsQuery
import com.inwords.expenses.feature.events.data.db.entity.PersonEntity
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.events.domain.model.Person

internal fun EventEntity.toDomain(): Event {
    return Event(
        id = eventId,
        name = name,
    )
}

internal fun EventWithDetailsQuery.toDomain(): EventDetails {
    return EventDetails(
        event = event.toDomain(),
        currencies = currencies.map { it.toDomain() },
        persons = persons.map { it.toDomain() },
        primaryCurrency = primaryCurrency.toDomain(),
        primaryPerson = primaryPerson.toDomain(),
    )
}

internal fun PersonEntity.toDomain() = Person(
    id = personId,
    name = name,
)

internal fun CurrencyEntity.toDomain(): Currency {
    return Currency(
        id = currencyId,
        code = code,
        name = name,
    )
}