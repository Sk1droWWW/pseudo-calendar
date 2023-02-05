package int20h.troipsa.pseudocalendar.ui.main

import dagger.hilt.android.lifecycle.HiltViewModel
import int20h.troipsa.pseudocalendar.data.prefs.PseudoPrefsManager
import int20h.troipsa.pseudocalendar.data.prefs.base.get
import int20h.troipsa.pseudocalendar.data.prefs.base.set
import int20h.troipsa.pseudocalendar.domain.interactors.AddEventInteractor
import int20h.troipsa.pseudocalendar.domain.interactors.GetUserFlowInteractor
import int20h.troipsa.pseudocalendar.domain.models.Event
import int20h.troipsa.pseudocalendar.ui.base.view_model.BaseViewModel
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

    fun generateEvents() {
        runCoroutine {
            if (!prefsManager.isEventsGenerated.get()) {
                addEventInteractor(Event.generateEvents())
                prefsManager.isEventsGenerated.set(true)
            }
        }
    }



}