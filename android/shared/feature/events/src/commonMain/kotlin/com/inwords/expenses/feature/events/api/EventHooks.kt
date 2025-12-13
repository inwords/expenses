package com.inwords.expenses.feature.events.api

interface EventHooks {

    suspend fun onBeforeEventDeletion(eventId: Long)
}