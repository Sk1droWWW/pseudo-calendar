package int20h.troipsa.pseudocalendar.ui.event_type

import dagger.hilt.android.lifecycle.HiltViewModel
import int20h.troipsa.pseudocalendar.domain.interactors.AddEventTypeInteractor
import int20h.troipsa.pseudocalendar.domain.interactors.GetEventTypeInteractor
import int20h.troipsa.pseudocalendar.domain.models.EventType
import int20h.troipsa.pseudocalendar.ui.base.view_model.BaseViewModel
import int20h.troipsa.pseudocalendar.utils.coroutines.launchAndCollect
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class EventTypeViewModel @Inject constructor(
    private val getEventTypeInteractor: GetEventTypeInteractor,
    private val addEventTypeInteractor: AddEventTypeInteractor,
) : BaseViewModel() {

    private val _eventType = MutableStateFlow(EventType.default)
    val eventType = _eventType.asStateFlow()

    private val _closeScreen = MutableSharedFlow<Unit>()
    val closeScreen = _closeScreen.asSharedFlow()

    fun loadEventType(id: Int) {
        if (id == 0) return
        getEventTypeInteractor(id).launchAndCollect(scope) { type ->
            _eventType.value = type
        }
    }

    fun updateEventTypeName(name: String) {
        _eventType.value = _eventType.value.copy(name = name)
    }

    fun updateEventTypeColor(color: Long) {
        _eventType.value = _eventType.value.copy(color = color)
    }

    fun saveEventType() {
        runCoroutine {
            addEventTypeInteractor(_eventType.value)
            closeScreen()
        }
    }

    private fun closeScreen() {
        runCoroutine {
            _closeScreen.emit(Unit)
        }
    }
}