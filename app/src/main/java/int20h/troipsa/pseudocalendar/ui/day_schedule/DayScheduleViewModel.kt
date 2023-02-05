package int20h.troipsa.pseudocalendar.ui.day_schedule

import dagger.hilt.android.lifecycle.HiltViewModel
import int20h.troipsa.pseudocalendar.domain.interactors.GetDayScheduleInteractor
import int20h.troipsa.pseudocalendar.domain.models.DaySchedule
import int20h.troipsa.pseudocalendar.ui.base.BaseViewModel
import int20h.troipsa.pseudocalendar.utils.coroutines.launchAndCollect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class DayScheduleViewModel @Inject constructor(
    private val getDayScheduleInteractor: GetDayScheduleInteractor
) : BaseViewModel() {

    private val _daySchedule = MutableStateFlow(DaySchedule(0, emptyList()))
    val daySchedule = _daySchedule.asStateFlow()

    fun loadDaySchedule(epochDay: Long) {
        runCoroutine {
            getDayScheduleInteractor(epochDay).launchAndCollect(this) {
                _daySchedule.value = it
            }
        }
    }
}