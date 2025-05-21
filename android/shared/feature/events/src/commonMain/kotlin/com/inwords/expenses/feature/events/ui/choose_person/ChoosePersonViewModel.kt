package com.inwords.expenses.feature.events.ui.choose_person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inwords.expenses.core.navigation.Destination
import com.inwords.expenses.core.navigation.NavigationController
import com.inwords.expenses.core.ui.utils.SimpleScreenState
import com.inwords.expenses.core.ui.utils.updateIfSuccess
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.core.utils.asImmutableListAdapter
import com.inwords.expenses.feature.events.domain.EventsInteractor
import com.inwords.expenses.feature.events.domain.model.Event
import com.inwords.expenses.feature.events.domain.model.Person
import com.inwords.expenses.feature.events.ui.choose_person.ChoosePersonScreenUiModel.PersonUiModel
import com.inwords.expenses.feature.settings.api.SettingsRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
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

    private val _state = MutableStateFlow<SimpleScreenState<ChoosePersonScreenUiModel>>(SimpleScreenState.Loading)
    val state: StateFlow<SimpleScreenState<ChoosePersonScreenUiModel>> = _state

    private var confirmJob: Job? = null

    init {
        combine(
            eventsInteractor.currentEvent
                .filterNotNull() // TODO mvp
                .map { EventWithPersons(event = it.event, persons = it.persons) }
                .distinctUntilChanged(),
            settingsRepository.getCurrentPersonId()
        ) { eventWithPersons, currentPersonId ->
            var needToSelectFirst = !eventWithPersons.persons.any { person -> person.id == currentPersonId }
            val persons = eventWithPersons.persons.map { person ->
                val model = PersonUiModel(
                    id = person.id,
                    name = person.name,
                    selected = person.id == currentPersonId || needToSelectFirst
                )
                needToSelectFirst = false
                model
            }

            _state.value = if (persons.isEmpty()) {
                SimpleScreenState.Empty
            } else {
                SimpleScreenState.Success(
                    ChoosePersonScreenUiModel(
                        eventId = eventWithPersons.event.id,
                        eventName = eventWithPersons.event.name,
                        persons = persons.asImmutableListAdapter(),
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onPersonSelected(personId: Long) {
        _state.updateIfSuccess { currentState ->
            ChoosePersonScreenUiModel(
                eventId = currentState.eventId,
                eventName = currentState.eventName,
                persons = currentState.persons.map { person ->
                    person.copy(selected = person.id == personId)
                }.asImmutableListAdapter()
            )
        }
    }

    fun onConfirmClicked() {
        confirmJob?.cancel()
        confirmJob = viewModelScope.launch {
            val currentState = _state.value as? SimpleScreenState.Success ?: return@launch

            val selectedPersonId = (currentState.data.persons.firstOrNull { it.selected }
                ?: currentState.data.persons.first()).id
            settingsRepository.setCurrentPersonId(selectedPersonId)
            navigationController.navigateTo(
                destination = expensesScreenDestination,
                popUpTo = expensesScreenDestination,
                launchSingleTop = true
            )
        }
    }

    private fun ChoosePersonScreenUiModel(
        eventId: Long,
        eventName: String,
        persons: ImmutableList<PersonUiModel>
    ): ChoosePersonScreenUiModel {
        return ChoosePersonScreenUiModel(
            eventId = eventId,
            eventName = eventName,
            persons = persons,
            selectedPersonName = persons.first { it.selected }.name
        )
    }

}