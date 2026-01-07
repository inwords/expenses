package com.inwords.expenses.feature.sync.data

import android.content.Context
import androidx.concurrent.futures.await
import androidx.work.WorkManager
import com.inwords.expenses.core.utils.IO
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

actual class EventsSyncManager internal constructor(
    private val context: Context
) {

    @OptIn(DelicateCoroutinesApi::class)
    private val scope = GlobalScope + IO

    private val mutex = Mutex()

    internal actual fun pushAllEventInfo(eventId: Long) {
        scope.launch {
            mutex.withLock {
                val workManager = WorkManager.getInstance(context)

                val workInfos = workManager.getWorkInfosByTagFlow(getTagForEvent(eventId)).first()

                if (workInfos.any { !it.state.isFinished }) {
                    return@launch
                }

                WorkManager.getInstance(context)
                    .beginWith(CurrenciesPullWorker.buildCurrenciesPullRequest(eventId))
                    .then(EventPushWorker.buildEventPushRequest(eventId))
                    .then(EventPersonsPushWorker.buildEventPersonsPushRequest(eventId))
                    .then(EventPullPersonsWorker.buildEventPullPersonsRequest(eventId))
                    .then(EventExpensesPushWorker.buildEventExpensesPushRequest(eventId))
                    .then(EventExpensesPullWorker.buildEventExpensesPullRequest(eventId))
                    .enqueue()
            }
        }
    }

    actual suspend fun cancelEventSync(eventId: Long) {
        withContext(IO) {
            mutex.withLock {
                val workManager = WorkManager.getInstance(context)
                workManager.cancelAllWorkByTag(getTagForEvent(eventId)).result.await()
            }
        }
    }

}

internal actual class EventsSyncManagerFactory(private val context: Context) {

    actual fun create(): EventsSyncManager {
        return EventsSyncManager(context)
    }
}
