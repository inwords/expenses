package com.inwords.expenses.feature.events.ui.dialog.delete

import androidx.lifecycle.ViewModel
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.feature.events.domain.DeleteEventUseCase
import com.inwords.expenses.feature.events.domain.DeleteEventUseCase.DeleteEventResult
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.domain.EventsInteractor.EventDeletionState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class DeleteEventDialogViewModel(
    private val navigationController: NavigationController,
    private val eventsInteractor: EventsInteractor,
    private val deleteEventUseCase: DeleteEventUseCase,
    private val eventId: Long,
) : ViewModel() {

    private var deleteJob: Job? = null

    fun onConfirmDelete() {
        if (deleteJob != null) return // prevent multiple clicks for deletion
        deleteJob = GlobalScope.launch(IO) {
            eventsInteractor.setEventDeletionState(eventId, EventDeletionState.Loading)
            navigationController.popBackStack()

            val result = try {
                deleteEventUseCase.deleteRemoteAndLocalEvent(eventId)
            } catch (e: CancellationException) {
                eventsInteractor.setEventDeletionState(eventId, EventDeletionState.RemoteDeletionFailed)
                throw e
            }
            when (result) {
                DeleteEventResult.Deleted -> {
                    // Deleted from DB, so will be reactively updated everywhere
                    eventsInteractor.clearEventDeletionState(eventId)
                }

                DeleteEventResult.RemoteFailed -> {
                    // Remote deletion failed, local is untouched
                    eventsInteractor.setEventDeletionState(eventId, EventDeletionState.RemoteDeletionFailed)
                }
            }
        }
    }

    fun onDismiss() {
        navigationController.popBackStack()
    }
}
