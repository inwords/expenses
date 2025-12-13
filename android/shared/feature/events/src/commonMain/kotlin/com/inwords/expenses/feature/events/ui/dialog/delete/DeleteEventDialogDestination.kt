package com.inwords.expenses.feature.events.ui.dialog.delete

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.scene.DialogSceneStrategy.Companion.dialog
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavModule
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.feature.events.domain.DeleteEventUseCase
import com.inwords.expenses.feature.events.domain.EventsInteractor
import kotlinx.serialization.Serializable

@Serializable
data class DeleteEventDialogDestination(
    val eventId: Long,
    val eventName: String,
) : Destination

fun getDeleteEventDialogNavModule(
    navigationController: NavigationController,
    eventsInteractor: EventsInteractor,
    deleteEventUseCase: DeleteEventUseCase,
): NavModule {
    return NavModule(DeleteEventDialogDestination.serializer()) {
        entry<DeleteEventDialogDestination>(metadata = dialog()) { key ->
            val viewModel = viewModel<DeleteEventDialogViewModel>(factory = viewModelFactory {
                initializer {
                    DeleteEventDialogViewModel(
                        navigationController = navigationController,
                        eventsInteractor = eventsInteractor,
                        deleteEventUseCase = deleteEventUseCase,
                        eventId = key.eventId
                    )
                }
            })
            DeleteEventDialog(
                state = DeleteEventDialogUiModel(
                    eventName = key.eventName
                ),
                onConfirmDelete = viewModel::onConfirmDelete,
                onDismiss = viewModel::onDismiss,
            )
        }
    }
}
