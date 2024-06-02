package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.feature.events.domain.model.Person

internal interface PersonsRepository {

    suspend fun insert(persons: List<Person>): List<Person>
}