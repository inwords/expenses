package com.inwords.expenses.feature.events.domain.store.local

import com.inwords.expenses.feature.events.domain.model.Person

internal interface PersonsLocalStore {

    suspend fun insert(persons: List<Person>): List<Person>
}