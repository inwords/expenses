package com.inwords.expenses.feature.events.data.db.store

import com.inwords.expenses.feature.events.data.db.converter.toEntity
import com.inwords.expenses.feature.events.data.db.dao.PersonsDao
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.events.domain.store.local.PersonsLocalStore

internal class PersonsLocalStoreImpl(
    personsDaoLazy: Lazy<PersonsDao>
) : PersonsLocalStore {

    private val personsDao by personsDaoLazy

    override suspend fun insertWithoutCrossRefs(persons: List<Person>): List<Person> {
        val personEntities = persons.map { it.toEntity() }
        val ids = personsDao.insert(personEntities)

        return persons.zip(ids) { person, id ->
            id.takeIf { it != -1L }?.let { person.copy(id = it) } ?: person
        }
    }

}
