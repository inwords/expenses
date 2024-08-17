package com.inwords.expenses.feature.events.domain.store.local

import com.inwords.expenses.feature.events.data.db.entity.EventPersonCrossRef
import com.inwords.expenses.feature.events.domain.model.Person

internal interface PersonsLocalStore {

    /**
     * Inserts [persons] into the local store.
     * **CAUTION**: does not insert cross-refs (e.g. [EventPersonCrossRef]).
     */
    suspend fun insertWithoutCrossRefs(persons: List<Person>): List<Person>
}