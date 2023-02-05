package int20h.troipsa.pseudocalendar.ui.main

import dagger.hilt.android.lifecycle.HiltViewModel
import int20h.troipsa.pseudocalendar.data.prefs.PseudoPrefsManager
import int20h.troipsa.pseudocalendar.data.prefs.base.get
import int20h.troipsa.pseudocalendar.data.prefs.base.set
import int20h.troipsa.pseudocalendar.domain.interactors.AddEventInteractor
import int20h.troipsa.pseudocalendar.domain.interactors.GetUserFlowInteractor
import int20h.troipsa.pseudocalendar.domain.models.Event
import int20h.troipsa.pseudocalendar.ui.base.BaseViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getUserFlowInteractor: GetUserFlowInteractor,
    private val addEventInteractor: AddEventInteractor,
    private val prefsManager: PseudoPrefsManager
) : BaseViewModel() {

    val user = getUserFlowInteractor()
        .stateIn(scope = scope, SharingStarted.Eagerly, null)

    init {
        generateEvents()
    }

    private fun generateEvents() {
        runCoroutine {
            if (!prefsManager.isEventsGenerated.get()) {
                Event.generateEvents().forEach { event ->
                    addEventInteractor.addEvent(event)
                }
                prefsManager.isEventsGenerated.set(true)
            }
        }
    }



}