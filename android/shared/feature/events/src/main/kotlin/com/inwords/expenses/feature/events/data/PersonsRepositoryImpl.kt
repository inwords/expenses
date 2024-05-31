package com.inwords.expenses.feature.events.data

import com.inwords.expenses.feature.events.data.db.converter.toEntity
import com.inwords.expenses.feature.events.data.db.dao.PersonsDao
import com.inwords.expenses.feature.events.domain.PersonsRepository
import com.inwords.expenses.feature.events.domain.model.Person

internal class PersonsRepositoryImpl(
    personsDaoLazy: Lazy<PersonsDao>
) : PersonsRepository {

    private val personsDao by personsDaoLazy

    override suspend fun insert(person: Person): Person {
        val id = personsDao.insert(person.toEntity()).takeIf { it != -1L }
        return if (id == null) {
            person
        } else {
            person.copy(id = id)
        }
    }

}


