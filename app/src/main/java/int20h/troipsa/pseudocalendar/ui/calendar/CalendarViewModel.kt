package int20h.troipsa.pseudocalendar.ui.calendar

import dagger.hilt.android.lifecycle.HiltViewModel
import int20h.troipsa.pseudocalendar.domain.interactors.GetEventsInteractor
import int20h.troipsa.pseudocalendar.ui.base.view_model.BaseViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    getEventsInteractor: GetEventsInteractor,
) : BaseViewModel() {

    val eventsMap = getEventsInteractor().stateIn(scope, SharingStarted.Eagerly, emptyMap())
}