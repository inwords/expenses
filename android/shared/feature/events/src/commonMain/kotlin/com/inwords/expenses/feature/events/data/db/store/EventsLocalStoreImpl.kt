package com.inwords.expenses.feature.events.data.db.store

import androidx.room.RoomDatabase
import com.inwords.expenses.feature.events.data.db.converter.toDomain
import com.inwords.expenses.feature.events.data.db.converter.toEntity
import com.inwords.expenses.feature.events.data.db.dao.EventsDao
import com.inwords.expenses.feature.events.data.db.entity.EventCurrencyCrossRef
import com.inwords.expenses.feature.events.data.db.entity.EventPersonCrossRef
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.events.domain.store.local.CurrenciesLocalStore
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import com.inwords.expenses.feature.events.domain.store.local.PersonsLocalStore
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class EventsLocalStoreImpl(
    dbLazy: Lazy<RoomDatabase>,
    eventsDaoLazy: Lazy<EventsDao>,
    personsLocalStoreLazy: Lazy<PersonsLocalStore>,
    currenciesLocalStoreLazy: Lazy<CurrenciesLocalStore>,
) : EventsLocalStore {

    private val db by dbLazy
    private val eventsDao by eventsDaoLazy
    private val personsRepository by personsLocalStoreLazy
    private val currenciesRepository by currenciesLocalStoreLazy

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

    override suspend fun update(eventId: Long, newServerId: Long): Boolean {
        return eventsDao.update(eventId, newServerId) == 1
    }

    override suspend fun deepInsert(
        eventToInsert: Event,
        personsToInsert: List<Person>,
        primaryCurrencyIndex: Int,
    ): EventDetails = coroutineScope {
        // FIXME: transaction not used (Fatal signal 11 (SIGSEGV), code 1 (SEGV_MAPERR), fault addr 0x0 in tid 23969 (DefaultDispatch), pid 23897 (nwords.expenses))
        val personsDeferred = async { personsRepository.insert(personsToInsert) }
        val currenciesDeferred = async { currenciesRepository.getCurrencies().first() }

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


