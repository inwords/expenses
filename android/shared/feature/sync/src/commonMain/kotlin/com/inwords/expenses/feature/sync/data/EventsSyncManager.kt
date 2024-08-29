package com.inwords.expenses.feature.sync.data

internal expect class EventsSyncManager {

    fun pushAllEventInfo(eventId: Long)
}

internal expect class EventsSyncManagerFactory {

    fun create(): EventsSyncManager
}