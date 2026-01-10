package com.inwords.expenses.feature.sync.data

import kotlinx.coroutines.flow.Flow

expect class EventsSyncManager {

    internal fun pushAllEventInfo(eventId: Long)

    suspend fun cancelEventSync(eventId: Long)

    internal fun getSyncState(): Flow<Set<Long>>

}

internal expect class EventsSyncManagerFactory {

    fun create(): EventsSyncManager
}
