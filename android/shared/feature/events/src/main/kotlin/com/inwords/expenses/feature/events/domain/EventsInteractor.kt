package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.events.domain.model.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class EventsInteractor internal constructor() {

    fun getEventDetails(event: Event): Flow<EventDetails> {
        val currency1 = Currency(
            id = 1,
            code = "USD",
            name = "US Dollar"
        )
        val currency2 = Currency(
            id = 2,
            code = "EUR",
            name = "Euro"
        )
        val currency3 = Currency(
            id = 3,
            code = "RUB",
            name = "Russian Ruble"
        )
        val person1 = Person(
            id = 1,
            name = "Василий"
        )
        val person2 = Person(
            id = 2,
            name = "Максим"
        )
        val person3 = Person(
            id = 3,
            name = "Анжела"
        )
        val person4 = Person(
            id = 4,
            name = "Саша"
        )
        return flowOf(
            EventDetails(
                event = event,
                currencies = listOf(currency1, currency2, currency3),
                persons = listOf(person1, person2, person3, person4),
                primaryCurrency = currency1,
                primaryPerson = person1,
            )
        )
    }

    fun getCurrentEvent(): Event {
        return Event(1L, "Fruska")
    }

}