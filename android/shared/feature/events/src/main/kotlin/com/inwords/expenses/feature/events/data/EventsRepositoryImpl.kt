package com.inwords.expenses.feature.events.data

import com.inwords.expenses.feature.events.data.db.converter.toDomain
import com.inwords.expenses.feature.events.data.db.converter.toEntity
import com.inwords.expenses.feature.events.data.db.dao.EventsDao
import com.inwords.expenses.feature.events.domain.EventsRepository
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.EventDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class EventsRepositoryImpl(
    eventsDaoLazy: Lazy<EventsDao>
) : EventsRepository {

    private val eventsDao by eventsDaoLazy

    override fun getEvents(): Flow<List<Event>> {
        return eventsDao.queryAll().map { entities ->
            entities.map { entity -> entity.toDomain() }
        }
    }

    override fun getEventWithDetails(eventId: Long): Flow<EventDetails> {
        return eventsDao.queryEventWithDetailsById(eventId).map { entity ->
            entity.toDomain()
        }
    }

    override suspend fun insert(event: EventDetails): EventDetails {
        val id = eventsDao.insert(event.toEntity()).takeIf { it != -1L }
        return if (id == null) {
            event
        } else {
            event.copy(event = event.event.copy(id = id))
        }
    }

}


