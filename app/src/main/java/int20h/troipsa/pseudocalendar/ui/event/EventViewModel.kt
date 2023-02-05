package int20h.troipsa.pseudocalendar.ui.event

import dagger.hilt.android.lifecycle.HiltViewModel
import int20h.troipsa.pseudocalendar.domain.interactors.GetEventInteractor
import int20h.troipsa.pseudocalendar.domain.models.Event
import int20h.troipsa.pseudocalendar.domain.models.EventType
import int20h.troipsa.pseudocalendar.ui.base.view_model.BaseViewModel
import int20h.troipsa.pseudocalendar.utils.coroutines.launchAndCollect
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val getEventInteractor: GetEventInteractor
) : BaseViewModel() {

    private val _event = MutableStateFlow(defaultEvent)
    val event = _event.asStateFlow()

    private val _updatedEvent = MutableStateFlow(_event.value)
    val updatedEvent = _updatedEvent.asStateFlow()

    val canSave = _event.combine(_updatedEvent) { event, updatedEvent ->
        event != updatedEvent
    }.stateIn(scope, SharingStarted.Eagerly, false)

    fun loadEvent(eventId: Int) {
        runCoroutine {
            getEventInteractor(eventId).launchAndCollect(this) { event ->
                _event.value = event
            }
        }
    }

    fun onNameChange(name: String) {
        _updatedEvent.value = _updatedEvent.value.copy(name = name)
    }

    fun saveEvent() {
        runCoroutine(
            withProgress = true,
        ) {

        }
    }

    companion object {
        private val defaultEvent = Event(
            id = 0,
            name = "",
            description = "",
            startTime = LocalDateTime.now(),
            endTime = LocalDateTime.now().plusMinutes(30),
            eventType = EventType(
                id = 0,
                name = "Event Type 0",
                color = 0xFF3700B3,
            ),
        )
    }
}