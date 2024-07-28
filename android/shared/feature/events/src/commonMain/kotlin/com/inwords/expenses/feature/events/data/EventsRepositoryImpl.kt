package com.inwords.expenses.feature.events.data

import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.inwords.expenses.feature.events.data.db.converter.toDomain
import com.inwords.expenses.feature.events.data.db.converter.toEntity
import com.inwords.expenses.feature.events.data.db.dao.EventsDao
import com.inwords.expenses.feature.events.data.db.entity.EventCurrencyCrossRef
import com.inwords.expenses.feature.events.data.db.entity.EventPersonCrossRef
import com.inwords.expenses.feature.events.domain.CurrenciesRepository
import com.inwords.expenses.feature.events.domain.EventsRepository
import com.inwords.expenses.feature.events.domain.PersonsRepository
import com.inwords.expenses.feature.events.domain.model.Currency
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.events.domain.model.Person
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class EventsRepositoryImpl(
    dbLazy: Lazy<RoomDatabase>,
    eventsDaoLazy: Lazy<EventsDao>,
    personsRepositoryLazy: Lazy<PersonsRepository>,
    currenciesRepositoryLazy: Lazy<CurrenciesRepository>,
) : EventsRepository {

    private val db by dbLazy
    private val eventsDao by eventsDaoLazy
    private val personsRepository by personsRepositoryLazy
    private val currenciesRepository by currenciesRepositoryLazy

    override fun getEvents(): Flow<List<Event>> {
        return eventsDao.queryAll().map { entities ->
            entities.map { entity -> entity.toDomain() }
        }
    }

    override fun getEvent(eventId: Long): Flow<Event> {
        return eventsDao.queryById(eventId).map { entity ->
            entity.toDomain()
        }
    }

    override fun getEventWithDetails(eventId: Long): Flow<EventDetails> {
        return eventsDao.queryEventWithDetailsById(eventId).map { entity ->
            entity.toDomain()
        }
    }

    override suspend fun deepInsert(
        eventToInsert: Event,
        personsToInsert: List<Person>,
        currenciesToInsert: List<Currency>,
        primaryCurrencyIndex: Int,
    ): EventDetails = db.withTransaction {
        coroutineScope {
            val personsDeferred = async { personsRepository.insert(personsToInsert) }
            val currenciesDeferred = async { currenciesRepository.insert(currenciesToInsert) }

            val persons = personsDeferred.await()
            val currencies = currenciesDeferred.await()

            val eventDetails = EventDetails(
                event = eventToInsert,
                persons = persons,
                currencies = currencies,
                primaryCurrency = currencies[primaryCurrencyIndex],
            )

            val eventId = eventsDao.insert(eventDetails.toEntity()).takeIf { it != -1L }
            val resultEventDetails = if (eventId == null) {
                eventDetails
            } else {
                eventDetails.copy(event = eventDetails.event.copy(id = eventId))
            }

            launch {
                val personCrossRefs = eventDetails.persons.map { person ->
                    EventPersonCrossRef(eventId = resultEventDetails.event.id, personId = person.id)
                }
                eventsDao.insertPersonCrossRef(personCrossRefs)
            }

            launch {
                val currencyCrossRefs = eventDetails.currencies.map { currency ->
                    EventCurrencyCrossRef(eventId = resultEventDetails.event.id, currencyId = currency.id)
                }
                eventsDao.insertCurrencyCrossRef(currencyCrossRefs)
            }

            resultEventDetails
        }
    }

}


