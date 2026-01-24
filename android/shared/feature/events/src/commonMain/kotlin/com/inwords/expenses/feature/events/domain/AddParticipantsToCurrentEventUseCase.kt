package com.inwords.expenses.feature.events.domain

import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.events.domain.store.local.EventsLocalStore
import com.inwords.expenses.feature.settings.api.SettingsRepository
import kotlinx.coroutines.flow.first

class AddParticipantsToCurrentEventUseCase internal constructor(
    eventsLocalStoreLazy: Lazy<EventsLocalStore>,
    settingsRepositoryLazy: Lazy<SettingsRepository>,
) {
    private val eventsLocalStore by eventsLocalStoreLazy
    private val settingsRepository by settingsRepositoryLazy

    suspend fun addParticipants(personNames: List<String>) {
        val eventId = settingsRepository.getCurrentEventId().first() ?: return
        val persons = personNames
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .map { name -> Person(id = 0L, serverId = null, name = name) }

        if (persons.isNotEmpty()) {
            eventsLocalStore.insertPersonsWithCrossRefs(eventId, persons, inTransaction = true)
        }
    }
}
