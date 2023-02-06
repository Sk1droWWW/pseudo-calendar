package int20h.troipsa.pseudocalendar.ui.event

import dagger.hilt.android.lifecycle.HiltViewModel
import int20h.troipsa.pseudocalendar.domain.interactors.AddEventInteractor
import int20h.troipsa.pseudocalendar.domain.interactors.DeleteEventInteractor
import int20h.troipsa.pseudocalendar.domain.interactors.GetEventInteractor
import int20h.troipsa.pseudocalendar.domain.models.Event
import int20h.troipsa.pseudocalendar.domain.models.EventType
import int20h.troipsa.pseudocalendar.ui.base.view_model.BaseViewModel
import int20h.troipsa.pseudocalendar.utils.coroutines.launchAndCollect
import int20h.troipsa.pseudocalendar.utils.extension.atTime
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val getEventInteractor: GetEventInteractor,
    private val addEventInteractor: AddEventInteractor,
    private val deleteEventInteractor: DeleteEventInteractor
) : BaseViewModel() {

    private val _event = MutableStateFlow(defaultEvent)
    val event = _event.asStateFlow()

    private val _updatedEvent = MutableStateFlow(_event.value)
    val updatedEvent = _updatedEvent.asStateFlow()

    val canSave = _event.combine(_updatedEvent) { event, updatedEvent ->
        event != updatedEvent
    }.stateIn(scope, SharingStarted.Eagerly, false)

    val canDelete = _event.map {
        it.id != 0
    }.stateIn(scope, SharingStarted.Eagerly, false)

    private val _closeScreen = MutableSharedFlow<Unit>()
    val closeScreen = _closeScreen.asSharedFlow()

    private val _dateDialogVisible = MutableStateFlow(false)
    val dateDialogVisible = _dateDialogVisible.asStateFlow()

    private val _timeDialogVisible = MutableStateFlow(false)
    val timeDialogVisible = _timeDialogVisible.asStateFlow()

    private val _timeFrame = MutableStateFlow(Timeframes.START)
    val timeFrame = _timeFrame.asStateFlow()

    fun loadEvent(eventId: Int) {
        runCoroutine {
            getEventInteractor(eventId).launchAndCollect(this) { event ->
                _event.value = event
                _updatedEvent.value = event
            }
        }
    }

    fun saveEvent() {
        runCoroutine {
            addEventInteractor(_updatedEvent.value)
            closeScreen()
        }
    }

    fun deleteEvent() {
        runCoroutine {
            deleteEventInteractor(_updatedEvent.value.id)
            closeScreen()
        }
    }

    fun onNameChange(name: String) {
        _updatedEvent.value = _updatedEvent.value.copy(name = name)
    }

    fun onDescriptionChange(description: String) {
        _updatedEvent.value = _updatedEvent.value.copy(description = description)
    }

    fun showDateDialog(show: Boolean = true) {
        _dateDialogVisible.value = show
    }

    fun showTimeDialog(
        show: Boolean = true,
        timeframe: Timeframes? = null
    ) {
        if (timeframe != null) {
            _timeFrame.value = timeframe
        }

        _timeDialogVisible.value = show
    }

    fun onDateChange(date: LocalDate) {
        _updatedEvent.value = _updatedEvent.value.copy(
            startTime = date.atStartOfDay(),
            endTime = date.atStartOfDay()
        )
        showDateDialog(false)
    }

    fun onTimeChange(time: LocalTime) {
        when (_timeFrame.value) {
            Timeframes.START -> {
                if (time < _updatedEvent.value.endTime.toLocalTime()) {
                    _updatedEvent.value = _updatedEvent.value.copy(
                        startTime = _updatedEvent.value.startTime.atTime(time)
                    )
                }
            }
            Timeframes.END -> {
                if (time > _updatedEvent.value.startTime.toLocalTime()) {
                    _updatedEvent.value = _updatedEvent.value.copy(
                        endTime = _updatedEvent.value.endTime.atTime(time)
                    )
                }
            }
        }
        showTimeDialog(false)
    }

    private fun closeScreen() {
        runCoroutine {
            _closeScreen.emit(Unit)
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