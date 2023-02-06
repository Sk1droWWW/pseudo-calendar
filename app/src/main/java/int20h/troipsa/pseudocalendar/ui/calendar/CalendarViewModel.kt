package int20h.troipsa.pseudocalendar.ui.calendar

import dagger.hilt.android.lifecycle.HiltViewModel
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
    getEventTypesInteractor: GetEventTypesInteractor
) : BaseViewModel() {

    val eventsMap = getEventsInteractor().stateIn(scope, SharingStarted.Eagerly, emptyMap())

    private val _eventTypes = MutableStateFlow<List<EventType>>(emptyList())
    val eventTypes = _eventTypes.asStateFlow()

    init {
        // TODO: add property "need to show" and update calendar showing only needed events
        getEventTypesInteractor().launchAndCollect(scope) { eventTypes ->
            _eventTypes.value = eventTypes
        }
    }
}