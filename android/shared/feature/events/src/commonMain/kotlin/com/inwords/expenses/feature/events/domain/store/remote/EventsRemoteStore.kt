package com.inwords.expenses.feature.events.domain.store.remote

import com.inwords.expenses.core.utils.Result
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.events.domain.model.Person

internal interface EventsRemoteStore {

    suspend fun getEvent(eventId: Long, pinCode: String): Result<EventDetails>

    suspend fun createEvent(
        name: String,
        pinCode: String,
        currencies: List<Currency>,
        primaryCurrencyId: Long,
        users: List<Person>,
    ): Result<EventDetails>

}