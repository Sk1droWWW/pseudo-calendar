package int20h.troipsa.pseudocalendar.ui.main

import dagger.hilt.android.lifecycle.HiltViewModel
import int20h.troipsa.pseudocalendar.domain.interactors.GetUserFlowInteractor
import int20h.troipsa.pseudocalendar.ui.base.BaseViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getUserFlowInteractor: GetUserFlowInteractor,
) : BaseViewModel() {

    val user = getUserFlowInteractor()
        .stateIn(scope = scope, SharingStarted.Eagerly, null)

}