package int20h.troipsa.pseudocalendar.ui.calendar

import dagger.hilt.android.lifecycle.HiltViewModel
import int20h.troipsa.pseudocalendar.domain.interactors.AddEventTypeInteractor
import int20h.troipsa.pseudocalendar.domain.interactors.GetEventTypesInteractor
import int20h.troipsa.pseudocalendar.domain.interactors.GetEventsInteractor
import int20h.troipsa.pseudocalendar.domain.models.EventType
import int20h.troipsa.pseudocalendar.ui.base.view_model.BaseViewModel
import int20h.troipsa.pseudocalendar.utils.coroutines.launchAndCollect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    getEventsInteractor: GetEventsInteractor,
    private val getEventTypesInteractor: GetEventTypesInteractor,
    private val addEventTypeInteractor: AddEventTypeInteractor
) : BaseViewModel() {

    val eventsMap = getEventsInteractor().stateIn(scope, SharingStarted.Eagerly, emptyMap())

    private val _eventTypes = MutableStateFlow<List<EventType>>(emptyList())

    private val _changedEventTypes = MutableStateFlow<List<EventType>>(emptyList())
    val changedEventTypes = _changedEventTypes.asStateFlow()

    init {
        getEventTypesInteractor().launchAndCollect(scope) { eventTypes ->
            _eventTypes.value = eventTypes
            _changedEventTypes.value = eventTypes
        }
    }

    fun resetEventTypes() {
        runCoroutine {
            _changedEventTypes.value = _eventTypes.value
        }
    }

    fun onEventTypeClick(eventType: EventType) {
        runCoroutine {
            _changedEventTypes.value =_changedEventTypes.value.map { oldEventType ->
                if(oldEventType.id == eventType.id) {
                    oldEventType.copy(visible = !oldEventType.visible)
                } else {
                    oldEventType
                }
            }
        }
    }

    fun onApplyFiltersClick() {
        runCoroutine(
            withProgress = true,
        ) {
            addEventTypeInteractor(_changedEventTypes.value)
        }
    }

}