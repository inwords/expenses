package com.inwords.expenses.feature.events.data

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkRequest
import androidx.work.WorkRequest.Companion.MIN_BACKOFF_MILLIS
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.inwords.expenses.core.locator.ComponentsMap
import com.inwords.expenses.core.locator.inject
import com.inwords.expenses.feature.events.api.EventsComponent
import java.util.concurrent.TimeUnit

internal class CurrenciesPullWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val eventsComponent by ComponentsMap.inject<EventsComponent>()

    override suspend fun doWork(): Result {
        eventsComponent.currenciesPullTask.value.pullCurrencies()

        return Result.success()
    }

    companion object {

        fun buildCurrenciesPullRequest(eventId: Long): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<CurrenciesPullWorker>()
                .setCommonParameters()
                .addTag(getTagForEvent(eventId))
                .build()
        }
    }
}

internal class EventPushWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val eventsComponent by ComponentsMap.inject<EventsComponent>()

    override suspend fun doWork(): Result {
        val eventId = inputData.getEventId() ?: return Result.failure()

        val success = eventsComponent.eventPushTask.value.pushEvent(eventId)

        return if (success) {
            Result.success()
        } else {
            Result.retry()
        }
    }

    companion object {

        fun buildEventPushRequest(eventId: Long): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<EventPushWorker>()
                .setCommonParameters()
                .setInputDataEventId(eventId)
                .addTag(getTagForEvent(eventId))
                .build()
        }
    }
}

internal class EventPersonsPushWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val eventsComponent by ComponentsMap.inject<EventsComponent>()

    override suspend fun doWork(): Result {
        val eventId = inputData.getEventId() ?: return Result.failure()

        val success = eventsComponent.eventPersonsPushTask.value.pushEventPersons(eventId)

        return if (success) {
            Result.success()
        } else {
            Result.retry()
        }
    }

    companion object {

        fun buildEventPersonsPushRequest(eventId: Long): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<EventPersonsPushWorker>()
                .setCommonParameters()
                .setInputDataEventId(eventId)
                .addTag(getTagForEvent(eventId))
                .build()
        }
    }
}

internal class EventPullCurrenciesAndPersonsWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private val eventsComponent by ComponentsMap.inject<EventsComponent>()

    override suspend fun doWork(): Result {
        val eventId = inputData.getEventId() ?: return Result.failure()

        val success = eventsComponent.eventPullCurrenciesAndPersonsTask.value.pullEventCurrenciesAndPersons(eventId)

        return when (success) {
            true -> Result.success()
            false -> Result.retry()
            null -> Result.failure()
        }
    }

    companion object {

        fun buildEventPullCurrenciesAndPersonsRequest(eventId: Long): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<EventPullCurrenciesAndPersonsWorker>()
                .setCommonParameters()
                .setInputDataEventId(eventId)
                .addTag(getTagForEvent(eventId))
                .build()
        }
    }
}

internal fun getTagForEvent(eventId: Long): String {
    return "$EVENTS_SYNC_WORKER_GROUP:$eventId"
}

private const val EVENTS_SYNC_WORKER_GROUP = "events_sync"

private const val KEY_EVENT_ID = "EVENT_ID"

private fun Data.getEventId(): Long? {
    val eventId = getLong(KEY_EVENT_ID, -1L)
    if (eventId == -1L) {
        // TODO log wtf
        return null
    }
    return eventId
}

private fun <B : WorkRequest.Builder<B, *>, W : WorkRequest> WorkRequest.Builder<B, W>.setInputDataEventId(eventId: Long): B {
    return setInputData(
        workDataOf(KEY_EVENT_ID to eventId)
    )
}

private fun <B : WorkRequest.Builder<B, *>, W : WorkRequest> WorkRequest.Builder<B, W>.setCommonParameters(): B {
    return setConstraints(Constraints(requiredNetworkType = NetworkType.CONNECTED))
        .setBackoffCriteria(BackoffPolicy.LINEAR, MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
        .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
}