package int20h.troipsa.pseudocalendar.ui.event

import dagger.hilt.android.lifecycle.HiltViewModel
import int20h.troipsa.pseudocalendar.domain.interactors.*
import int20h.troipsa.pseudocalendar.domain.models.Contact
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
    private val deleteEventInteractor: DeleteEventInteractor,
    private val getEventTypes: GetEventTypesInteractor,
    private val getContactListInteractor: GetContactListInteractor,
    private val getEventContactsInteractor: GetEventContactsInteractor,
    private val addEventContactsInteractor: AddEventContactsInteractor,
) : BaseViewModel() {

    private val _event = MutableStateFlow(defaultEvent)
    val event = _event.asStateFlow()

    private val _eventTypes = MutableStateFlow<List<EventType>>(emptyList())
    val eventTypes = _eventTypes.asStateFlow()

    private val _updatedEvent = MutableStateFlow(_event.value)
    val updatedEvent = _updatedEvent.asStateFlow()

    val canDelete = _event.map {
        it.id != 0
    }.stateIn(scope, SharingStarted.Eagerly, false)

    private val _closeScreen = MutableSharedFlow<Unit>()
    val closeScreen = _closeScreen.asSharedFlow()

    private val _dateDialogVisible = MutableStateFlow(false)
    val dateDialogVisible = _dateDialogVisible.asStateFlow()

    private val _timeDialogVisible = MutableStateFlow(false)
    val timeDialogVisible = _timeDialogVisible.asStateFlow()

    private val _contactsDialogVisible = MutableStateFlow(false)
    val contactsDialogVisible = _contactsDialogVisible.asStateFlow()

    private val _timeFrame = MutableStateFlow(Timeframes.START)
    val timeFrame = _timeFrame.asStateFlow()

    val allContacts = getContactListInteractor()
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    private val _eventContacts = MutableStateFlow<List<Contact>>(emptyList())

    private val _changedEventContacts = MutableStateFlow<List<Contact>>(emptyList())
    val changedEventContacts = _changedEventContacts.asStateFlow()

    val canSave = combine(
        _event,
        _updatedEvent,
        _changedEventContacts
    ) { event, updatedEvent, contacts ->
        (event != updatedEvent
                && updatedEvent.name.isNotEmpty()
                && updatedEvent.eventType.id != 0)
                || _eventContacts.value != contacts
    }.stateIn(scope, SharingStarted.Eagerly, false)

    fun initScreenInfo(eventId: Int) {
        runCoroutine {
            if (eventId == 0) {
                _event.value = defaultEvent
                _updatedEvent.value = defaultEvent
            } else {
                getEventInteractor(eventId).launchAndCollect(this) { event ->
                    _event.value = event
                    _updatedEvent.value = event
                }
            }
            getEventContactsInteractor(eventId).launchAndCollect(this) { contacts ->
                _changedEventContacts.value = contacts.toMutableList()
                _eventContacts.value = contacts
            }
            getEventTypes().launchAndCollect(this) { eventTypes ->
                _eventTypes.value = eventTypes
            }
        }
    }

    fun saveEvent() {
        runCoroutine {
            val event = _updatedEvent.value
            addEventInteractor(
                event.copy(epochDay = event.startTime.toLocalDate().toEpochDay())
            )
            _changedEventContacts.value.forEach { contact ->
                addEventContactsInteractor(event.id, contact.id)
            }

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

    fun showContactsDialog(show: Boolean = true) {
        _contactsDialogVisible.value = show
    }

    fun onContactClick(contact: Contact) {
        _changedEventContacts.value = _changedEventContacts.value + contact
    }

    fun onDateChange(date: LocalDate) {
        val startTime = _updatedEvent.value.startTime.toLocalTime()
        val endTime = _updatedEvent.value.endTime.toLocalTime()

        _updatedEvent.value = _updatedEvent.value.copy(
            startTime = date.atTime(startTime),
            endTime = date.atTime(endTime)
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

    fun onEventTypeChange(eventType: EventType) {
        _updatedEvent.value = _updatedEvent.value.copy(eventType = eventType)
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
                visible = true
            ),
        )
    }
}