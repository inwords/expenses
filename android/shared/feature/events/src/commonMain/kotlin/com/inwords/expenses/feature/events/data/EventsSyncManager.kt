package com.inwords.expenses.feature.events.data

internal expect class EventsSyncManager {

    fun pushAllEventInfo(eventId: Long)
}

internal expect class EventsSyncManagerFactory {

    fun create(): EventsSyncManager
}