package com.inwords.expenses.feature.sync.data

import android.content.Context
import androidx.work.WorkManager
import com.inwords.expenses.core.utils.IO
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal actual class EventsSyncManager(
    private val context: Context
) {

    @OptIn(DelicateCoroutinesApi::class)
    private val scope = GlobalScope + IO

    private val mutex = Mutex()

    actual fun pushAllEventInfo(eventId: Long) {
        scope.launch {
            mutex.withLock {
                val workManager = WorkManager.getInstance(context)
                workManager.cancelAllWork() // FIXME: for dev app

                val workInfos = workManager.getWorkInfosByTagFlow(getTagForEvent(eventId)).first()

                if (workInfos.any { !it.state.isFinished }) {
                    return@launch
                }

                WorkManager.getInstance(context)
                    .beginWith(CurrenciesPullWorker.buildCurrenciesPullRequest(eventId))
                    .then(EventPushWorker.buildEventPushRequest(eventId))
                    .then(
                        listOf(
                            EventPersonsPushWorker.buildEventPersonsPushRequest(eventId),
                            EventPullCurrenciesAndPersonsWorker.buildEventPullCurrenciesAndPersonsRequest(eventId)
                        )
                    )
                    .then(
                        listOf(
                            EventExpensesPushWorker.buildEventExpensesPushRequest(eventId),
                            EventExpensesPullWorker.buildEventExpensesPullRequest(eventId)
                        )
                    )
                    .enqueue()
            }
        }
    }

}

internal actual class EventsSyncManagerFactory(private val context: Context) {

    actual fun create(): EventsSyncManager {
        return EventsSyncManager(context)
    }
}