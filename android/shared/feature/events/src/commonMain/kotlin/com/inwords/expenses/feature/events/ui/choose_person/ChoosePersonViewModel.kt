package com.inwords.expenses.feature.events.ui.choose_person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.asImmutableListAdapter
import com.inwords.expenses.core.utils.flatMapLatestNoBuffer
import com.inwords.expenses.core.utils.stateInWhileSubscribed
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.events.ui.choose_person.ChoosePersonScreenUiModel.PersonUiModel
import com.inwords.expenses.feature.settings.api.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class ChoosePersonViewModel(
    private val navigationController: NavigationController,
    eventsInteractor: EventsInteractor,
    private val settingsRepository: SettingsRepository,
    private val expensesScreenDestination: Destination,
) : ViewModel(viewModelScope = CoroutineScope(SupervisorJob() + IO)) {

    private data class EventWithPersons(
        val event: Event,
        val persons: List<Person>
    )

    private var confirmJob: Job? = null

    private val selectedPersonId = MutableStateFlow<Long?>(null)

    val state: StateFlow<SimpleScreenState<ChoosePersonScreenUiModel>> = combine(
        eventsInteractor.currentEvent
            .filterNotNull() // TODO mvp
            .map { EventWithPersons(event = it.event, persons = it.persons) }
            .distinctUntilChanged(),
        selectedPersonId.flatMapLatestNoBuffer { selectedPersonId ->
            if (selectedPersonId == null) {
                settingsRepository.getCurrentPersonId()
            } else {
                flowOf(selectedPersonId)
            }
        },
    ) { eventWithPersons, selectedPersonId ->
        if (eventWithPersons.persons.isEmpty()) {
            return@combine SimpleScreenState.Empty
        }

        val personIdToSelect: Long = selectedPersonId ?: eventWithPersons.persons.first().id
        val persons = eventWithPersons.persons.map { person ->
            PersonUiModel(
                id = person.id,
                name = person.name,
                selected = person.id == personIdToSelect
            )
        }

        SimpleScreenState.Success(
            ChoosePersonScreenUiModel(
                eventId = eventWithPersons.event.id,
                eventName = eventWithPersons.event.name,
                persons = persons.asImmutableListAdapter(),
            )
        )
    }.stateInWhileSubscribed(viewModelScope, SimpleScreenState.Loading)

    fun onPersonSelected(personId: Long) {
        selectedPersonId.value = personId
    }

    fun onConfirmClicked() {
        confirmJob?.cancel()
        confirmJob = viewModelScope.launch {
            val currentState = state.value as? SimpleScreenState.Success ?: return@launch

            val selectedPersonId = currentState.data.persons.first { it.selected }.id
            settingsRepository.setCurrentPersonId(selectedPersonId)
            navigationController.navigateTo(
                destination = expensesScreenDestination,
                popUpTo = expensesScreenDestination,
                launchSingleTop = true
            )
        }
    }

}