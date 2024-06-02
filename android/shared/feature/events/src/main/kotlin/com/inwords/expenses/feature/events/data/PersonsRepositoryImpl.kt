package com.inwords.expenses.feature.events.data

import com.inwords.expenses.feature.events.data.db.converter.toEntity
import com.inwords.expenses.feature.events.data.db.dao.PersonsDao
import com.inwords.expenses.feature.events.domain.PersonsRepository
import com.inwords.expenses.feature.events.domain.model.Person

internal class PersonsRepositoryImpl(
    personsDaoLazy: Lazy<PersonsDao>
) : PersonsRepository {

    private val personsDao by personsDaoLazy

    override suspend fun insert(persons: List<Person>): List<Person> {
        val personEntities = persons.map { it.toEntity() }
        val ids = personsDao.insert(personEntities)

        return persons.zip(ids) { person, id ->
            id.takeIf { it != -1L }?.let { person.copy(id = it) } ?: person
        }
    }

}


