package com.inwords.expenses.feature.sync.data

expect class EventsSyncManager {

    internal fun pushAllEventInfo(eventId: Long)

    suspend fun cancelEventSync(eventId: Long)
}

internal expect class EventsSyncManagerFactory {

    fun create(): EventsSyncManager
}